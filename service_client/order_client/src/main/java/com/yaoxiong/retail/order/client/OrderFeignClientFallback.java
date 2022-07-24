package com.yaoxiong.retail.order.client;

import com.yaoxiong.retail.base.utils.Result;

public class OrderFeignClientFallback implements OrderFeignClient{
    @Override
    public Result getOrderDetailByOrderNumber(String orderNumber) {
        return Result.error().message("fail fast");
    }
}
