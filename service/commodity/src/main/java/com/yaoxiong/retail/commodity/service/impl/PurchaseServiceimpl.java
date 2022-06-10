package com.yaoxiong.retail.commodity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yaoxiong.retail.base.utils.Result;
import com.yaoxiong.retail.commodity.mapper.PurchaseMapper;
import com.yaoxiong.retail.commodity.service.CommodityService;
import com.yaoxiong.retail.commodity.service.PurchaseService;
import com.yaoxiong.retail.model.Commodity;
import com.yaoxiong.retail.model.Purchase;
import com.yaoxiong.retail.vo.PurchaseQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class PurchaseServiceimpl extends
        ServiceImpl<PurchaseMapper, Purchase> implements PurchaseService {
    @Autowired
    private CommodityService commodityService;

    @Transactional
    @Override
    public Result savePurchase(Purchase purchase) {
        try {
            purchase.setCreateDate(new Date());
            purchase.setRemain(purchase.getPurchaseQuantity());
            Commodity commodity = commodityService.getById(purchase.getCommodityId());
            commodity.setRemain(commodity.getRemain()+purchase.getPurchaseQuantity());

            this.save(purchase);
            commodityService.updateById(commodity);
            return Result.ok().data("data", purchase);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error().message("Failed to add purchase");
        }
    }

    @Override
    public Result getPurchaseList(PurchaseQueryVo purchaseQueryVo) {
        if (purchaseQueryVo == null)
            return Result.error().message("Parameter error");

        Integer commodityId = purchaseQueryVo.getCommodityId();
        Date createDateRangeStart = purchaseQueryVo.getCreateDateRangeStart();
        Date createDateRangeEnd = purchaseQueryVo.getCreateDateRangeEnd();

        try {
            QueryWrapper<Purchase> wrapper = new QueryWrapper<>();
            if (commodityId != null && commodityId > 0) {
                wrapper.eq("commodity_id", commodityId);
            }
            if (createDateRangeStart != null && createDateRangeEnd != null &&
                    createDateRangeEnd.getTime() > createDateRangeStart.getTime()) {
                wrapper.gt("create_date", createDateRangeStart)
                        .le("create_date", createDateRangeEnd);
            }
            return Result.ok().data("list", baseMapper.selectList(wrapper));

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error().message("error：" + e.getMessage());
        }
    }

    @Override
    public Result updataPurchase(Purchase purchase) {
        if (purchase == null || purchase.getId() == null || purchase.getId() <= 0) {
            return Result.error().message("Update failed, parameter error");
        }
        try {
            boolean flag = this.updateById(purchase);
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
    public List<Purchase> getRemainPurchaseList(Integer commodityId) throws Exception {
        if (commodityId == null || commodityId <= 0)
            throw new Exception("commodity id can not be null");

        QueryWrapper<Purchase> wrapper = new QueryWrapper<>();
        wrapper.eq("commodity_id", commodityId).gt("remain", 0).orderByAsc("create_date");
        return baseMapper.selectList(wrapper);
    }
}
