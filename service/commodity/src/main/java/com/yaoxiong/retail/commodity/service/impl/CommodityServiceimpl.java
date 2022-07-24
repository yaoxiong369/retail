package com.yaoxiong.retail.commodity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yaoxiong.retail.base.utils.Result;
import com.yaoxiong.retail.commodity.mapper.CommodityMapper;
import com.yaoxiong.retail.commodity.service.CategoryService;
import com.yaoxiong.retail.commodity.service.CommodityService;
import com.yaoxiong.retail.commodity.service.PurchaseService;
import com.yaoxiong.retail.commodity.utils.StringToDateConverter;
import com.yaoxiong.retail.model.Commodity;
import com.yaoxiong.retail.model.OrderDetail;
import com.yaoxiong.retail.model.Purchase;
import com.yaoxiong.retail.order.client.OrderFeignClient;
import com.yaoxiong.retail.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommodityServiceimpl extends
        ServiceImpl<CommodityMapper, Commodity> implements CommodityService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Override
    public Result getCommodityById(Integer id) {
        try {
            return Result.ok().data("object", this.getById(id));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error().message("get commodity fail !");
        }
    }

    @Override
    public Result saveCommodity(Commodity commodity) {
        try {
            this.save(commodity);
            return Result.ok().data("data", commodity);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error().message("Failed to add commodity");
        }

    }

    @Override
    public Result updataCommodity(Commodity commodity) {
        if (commodity == null || commodity.getId() == null || commodity.getId() <= 0) {
            return Result.error().message("Update failed, parameter error");
        }
        try {
            boolean flag = this.updateById(commodity);
            if (flag) {
                return Result.ok().message("Update successful");
            } else {
                return Result.error().message("Update failed");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error().message("Update failed:" + e.getMessage());
        }
    }

    @Override
    public Result selectCommodityPage(Page<Commodity> pageParam, CommodityQueryVo commodityQueryVo) {
        if (pageParam == null || commodityQueryVo == null)
            return Result.error().message("Parameter error");

        String keyWord = commodityQueryVo.getSearchKeyWord();
        Integer categoryId = commodityQueryVo.getCategoryId();

        try {
            QueryWrapper<Commodity> wrapper = new QueryWrapper<>();
            wrapper.orderByDesc("id");
            if (!StringUtils.isEmpty(keyWord)) {
                wrapper.like("name", keyWord)
                        .or().like("remarks", keyWord);
            }
            if (categoryId != null && categoryId.intValue() > 0) {
                wrapper.eq("category_id", categoryId);
            }

            //调用mapper的方法
            IPage<Commodity> pages = baseMapper.selectPage(pageParam, wrapper);

            return Result.ok().data("commodityList", pages.getRecords())
                    .data("total", pages.getTotal());
//                    .data("categoryList", categoryService.getCategoryListByCode(101))
//                    .data("unitList", categoryService.getCategoryListByCode(102));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error().message("error：" + e.getMessage());
        }

    }

    @Override
    public Result getCommodityWithOrder(Page<CommodityWithOrder> pageParam, CommodityWithOrderQueryVo commodityWithOrderQueryVo) {
        if (pageParam == null)
            return Result.error().message("Parameter error");
        if (commodityWithOrderQueryVo == null) {
            commodityWithOrderQueryVo = new CommodityWithOrderQueryVo();
        }
        try {
            List<CommodityWithOrder> reList = baseMapper.getCommodityWithOrderList(pageParam, commodityWithOrderQueryVo);
            Pair<Double,Double> pair = getTotalPriceAndItemCountByOrderNumber(commodityWithOrderQueryVo.getOrderNumber());
            return Result.ok().data("object", reList).data("totalPrice",  (double)Math.round(pair.getLeft() * 100) / 100).data("itemCount", pair.getRight());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error().message(e.getMessage());
        }

    }

    private Pair<Double, Double> getTotalPriceAndItemCountByOrderNumber(String orderNumber) throws InvocationTargetException, IllegalAccessException {
        Result result = orderFeignClient.getOrderDetailByOrderNumber(orderNumber);
        Double totalPrice = 0.0;
        Double itemCount = 0.0;
        OrderDetail orderDetail = new OrderDetail();

        if (result != null && result.getSuccess()) {
            List<Object> reList = (List<Object>) result.getData().get("object");
            if (reList != null) {
                for (Object object : reList) {
                    ConvertUtils
                            .register(new StringToDateConverter(), Date.class);
                    org.apache.commons.beanutils.BeanUtils.populate(orderDetail, (Map<String, Object>) object);
                    totalPrice += (orderDetail.getQuantity() * orderDetail.getSellPrice());
                    itemCount += orderDetail.getQuantity();
                }
            }
        }
        return Pair.of(totalPrice,itemCount);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateCommodityAndPurchaseRemain(OrderVo orderVo) throws Exception {
        if (orderVo == null || StringUtils.isEmpty(orderVo.getOrderNumber()) ||
                StringUtils.isEmpty(orderVo.getName()) || orderVo.getSellPrice() < 0 ||
                orderVo.getCommodityId().intValue() <= 0)
            return Result.error().message("Parameter error");

        Double amount = 0.0;
        List<Purchase> purchaseList = new ArrayList<>();
        Triple<Double, List<Purchase>, Double> triple = null;
        Commodity commodity = commodityService.getById(orderVo.getCommodityId());

        try {
            //update purchases remain
            if (orderVo.getPurchases() == null || orderVo.getPurchases().size() <= 0) {
                purchaseList = purchaseService.getRemainPurchaseList(orderVo.getCommodityId());
                triple = getProfitAndUpdatePurchaseList(orderVo.getSellPrice(), purchaseList, orderVo.getQuantity());
                List<PurchaseInOrderVo> purchaseInOrderVoList = new ArrayList<>();
                for (Purchase p : purchaseList) {
                    PurchaseInOrderVo purchaseInOrderVo = new PurchaseInOrderVo();
                    BeanUtils.copyProperties(p, purchaseInOrderVo);
                    purchaseInOrderVoList.add(purchaseInOrderVo);
                }
                orderVo.setPurchases(purchaseInOrderVoList);
            } else {
                purchaseList = purchaseService.listByIds(orderVo.getPurchases().stream().map(t -> t.getId()).collect(Collectors.toList()));
                Map<Integer, Double> map = orderVo.getPurchases().stream().collect(HashMap::new, (m, v) -> m.put(v.getId(), v.getSalesVolume()), HashMap::putAll);
//                        .collect(Collectors.toMap(PurchaseInOrderVo::getId, PurchaseInOrderVo::getSalesVolume));
                triple = getProfitAndUpdatePurchaseList(orderVo.getSellPrice(), purchaseList, map);
            }
            orderVo.setProfit(triple.getLeft());
            purchaseService.updateBatchById(triple.getMiddle());
            amount = triple.getRight();

            //update commdity remain
            if (commodity.getRemain() < amount)
                throw new Exception("Insufficient inventory of goods！");
            commodity.setRemain(commodity.getRemain() - amount);
            commodityService.updateById(commodity);
            orderVo.setQuantity(amount);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new Exception("update remain fail");
        }

        return Result.ok().data("data", orderVo);
    }

    private Triple<Double, List<Purchase>, Double> getProfitAndUpdatePurchaseList(Double sellPrice, List<Purchase> purchaseList, Map<Integer, Double> map) throws Exception {
        Assert.isTrue(sellPrice != null && sellPrice > 0, "Missing sell price parameter");
        Assert.isTrue(purchaseList != null && purchaseList.size() > 0, "purchase list cannot be null");
        Assert.isTrue(map != null && !map.isEmpty(), "Missing quantity parameter");

        Double profit = 0.0;
        Double amount = 0.0;
        List<Purchase> reList = new ArrayList<>(purchaseList);
        reList.stream().filter(t -> map.get(t.getId()) != null)
                .collect(Collectors.toList()).stream()
                .forEach(t -> {
                    t.setRemain(t.getRemain() - map.get(t.getId()));
                });

        for (Purchase purchase : reList) {
            if (purchase.getRemain() < 0)
                throw new Exception("Failed to add order");
            profit += ((sellPrice - purchase.getPurchasePrice()) * map.get(purchase.getId()));
            amount += map.get(purchase.getId());
        }

        return new ImmutableTriple<>(profit, reList, amount);
    }


    private Triple<Double, List<Purchase>, Double> getProfitAndUpdatePurchaseList(Double sellPrice, List<Purchase> purchaseList, Double quantity) throws Exception {
        Assert.isTrue(sellPrice != null && sellPrice > 0, "Missing sell price parameter");
        Assert.isTrue(purchaseList != null && purchaseList.size() > 0, "purchase list cannot be null");
        Assert.isTrue(quantity != null && quantity > 0, "Missing quantity parameter");

        Double profit = 0.0;
        Double amount = 0.0;
        List<Purchase> reList = new ArrayList<>();


        for (Purchase purchase : purchaseList) {
            if (purchase.getRemain() >= quantity) {
                profit = profit + ((sellPrice - purchase.getPurchasePrice()) * quantity);
                amount += quantity;
                purchase.setRemain(purchase.getRemain() - quantity);
                quantity = 0.0;
                reList.add(purchase);
                break;
            } else {
                profit = profit + ((sellPrice - purchase.getPurchasePrice()) * purchase.getRemain());
                quantity -= purchase.getRemain();
                amount += purchase.getRemain();
                purchase.setRemain(0.0);
                reList.add(purchase);
            }
        }

        if (quantity > 0) {
            throw new Exception("Requested quantity is greater than stock");
        }

        return new ImmutableTriple<>(profit, reList, amount);
    }


    public static void main(String[] args) {
        Double a = 1.0;
        Double b = a;
        Double c = a.doubleValue();
        String[] arr = {"1213", "1234", "123"};
        List<String> list = Arrays.asList(arr);
        List<String> list2 = null;

        for (String str : list2) {
            System.out.println("args = " + str);
        }

        b = 2.0;
        new CommodityServiceimpl().kk(c, list);
        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println("c = " + c);
        System.out.println("arr = " + list);

    }

    public void kk(Double aa, List<String> list) {
        list = list.stream().filter(t -> t.equals("123")).collect(Collectors.toList());
        aa = 11.0;

    }

}
