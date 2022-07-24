package com.yaoxiong.retail.commodity.client;

import com.yaoxiong.retail.base.utils.Result;
import com.yaoxiong.retail.vo.OrderVo;

public class CommodityFeignClientFallback implements CommodityFeignClient
{

    @Override
    public Result updateCommodityAndPurchaseRemain(OrderVo orderVo) {
        return Result.error().message("fail fast");
    }
}
