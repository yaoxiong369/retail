package com.yaoxiong.retail.order.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yaoxiong.retail.base.utils.Result;
import com.yaoxiong.retail.enums.OrderDetailStatusEnum;
import com.yaoxiong.retail.enums.OrderStatusEnum;
import com.yaoxiong.retail.model.CustomerOrder;
import com.yaoxiong.retail.order.service.OrderService;
import com.yaoxiong.retail.vo.DateAggregation;
import com.yaoxiong.retail.vo.OrderWithCommodityVo;
import com.yaoxiong.retail.commodity.client.CommodityFeignClient;
import com.yaoxiong.retail.model.OrderDetail;
import com.yaoxiong.retail.order.mapper.OrderDetailMapper;
import com.yaoxiong.retail.order.service.OrderDetailService;
import com.yaoxiong.retail.vo.OrderVo;
import com.yaoxiong.retail.vo.PlaceOrderDetailVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderDetailServiceimpl extends
        ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

    @Autowired
    private CommodityFeignClient commodityFeignClient;

    @Autowired
    private OrderService orderService;

    @Override
    public Result saveOrderDetail(OrderDetail orderDetail) {
        try {
            orderDetail.setCreateDate(new Date());
            this.save(orderDetail);
            return Result.ok().data("data", orderDetail);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error().message("Failed to add order details");
        }
    }

    @Override
//    @Transactional
    public Result saveOrderDetail(OrderVo orderVo) throws Exception {
        if (orderVo == null || StringUtils.isEmpty(orderVo.getOrderNumber()) ||
                StringUtils.isEmpty(orderVo.getName()) || orderVo.getSellPrice() < 0 ||
                orderVo.getCommodityId().intValue() <= 0 || (orderVo.getQuantity() != null && orderVo.getQuantity() <= 0))
            return Result.error().message("Parameter error");

        //TODO: (replace with distributed transactions) --> (RocketMQ:Dead-LetterMessage,Retry Queue,Ack)
        OrderDetail orderDetail = new OrderDetail();

        Result result = commodityFeignClient.updateCommodityAndPurchaseRemain(orderVo);

        if (result.getSuccess()) {
            BeanUtils.populate(orderDetail, (Map<String, Object>) (result.getData().get("data")));
//            orderDetail = BeanUtils.mapToBean((Map<String, Object>) (result.getData().get("data")),OrderDetail.class);
//            BeanUtils.copyProperties((OrderVo)(result.getData().get("data")), orderDetail);
            result = saveOrderDetail(orderDetail);
            if (!result.getSuccess()) {
                //TODO:restore the remain
            }
        }

        return result;
    }

    @Override
    public Result getOrderDetailByorderNumber(String orderNumber) throws Exception {
        try {
            QueryWrapper<OrderDetail> wrapper = new QueryWrapper<>();
            wrapper.eq("order_number", orderNumber);
            List<OrderDetail> reList = baseMapper.selectList(wrapper);
            return Result.ok().data("object", reList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error().message("error:" + e.getMessage());
        }
    }

    @Override
    public Result getPlaceOrderDetailVo(String orderNumber) throws Exception {
        try {
            List<OrderWithCommodityVo> orderWithCommodityList = baseMapper.getOrderWithCommodityList(orderNumber);
            List<PlaceOrderDetailVo> reList = new ArrayList<>();
            if (orderWithCommodityList != null && !orderWithCommodityList.isEmpty()) {
                for (OrderWithCommodityVo p : orderWithCommodityList) {
                    PlaceOrderDetailVo placeOrderDetailVo = new PlaceOrderDetailVo();
                    BeanUtils.copyProperties(placeOrderDetailVo, p);
                    placeOrderDetailVo.setPicture(p.getCommodity().getPicture());
                    reList.add(placeOrderDetailVo);
                }
            }
            return Result.ok().data("object", reList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error().message("error:" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result placeOrderDetail(CustomerOrder customerOrder) throws Exception {
        if (customerOrder == null || StringUtils.isEmpty(customerOrder.getOrderNumber())) {
            return Result.error().message("Order submission failed, parameter problem");
        }
        try {
            LambdaUpdateWrapper<OrderDetail> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(OrderDetail::getOrderNumber, customerOrder.getOrderNumber())
                    .set(OrderDetail::getStatus, OrderDetailStatusEnum.PLACE.getStatus());

            this.update(null, lambdaUpdateWrapper);
            orderService.saveOrder(customerOrder.getOrderNumber(), customerOrder.getRemarks(), customerOrder.getCustomerNumber());
            return Result.ok().message("Order placed successfully");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error().message("Failed to place order:" + e.getMessage());
        }

    }

    @Override
    public void deleteOrderDetail(String orderNumber) throws Exception {
        if (StringUtils.isEmpty(orderNumber)) {
            throw new Exception("Parameter error");
        }
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("order_number", orderNumber);
        updateWrapper.set("status", OrderDetailStatusEnum.DELETE.getStatus());
        baseMapper.update(null, updateWrapper);
    }

    @Override
    public Result revenueDayAggregation(Integer intervalDay) throws Exception {
        if (intervalDay == null || intervalDay.intValue() < 0) {
            return Result.error().message("Parameter error");
        }
        try {
            List<DateAggregation> aggregationList = baseMapper.revenueAggregation(intervalDay);
            Map<String, String> aggregationMap = aggregationList.stream().collect(Collectors.toMap(DateAggregation::getTimeGranularity, DateAggregation::getIndice, (key1, key2) -> key2));
            List<String> reList = new LinkedList<>();
            Date now = new Date();

            for (int i = 29; i >= 0; i--) {
                String day = new SimpleDateFormat("yyy-MM-dd").format(DateUtils.addDays(now, -i));
                reList.add(aggregationMap.get(day) == null ? "0" : aggregationMap.get(day).toString());
            }

            return Result.ok().data("object", reList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error().message("error：" + e.getMessage());
        }
    }

}
