package com.yaoxiong.retail.order.client;

import com.yaoxiong.retail.base.utils.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "order-server")
@Repository
public interface OrderFeignClient {

    @GetMapping("/api/order/detail/{orderNumber}")
    public Result getOrderDetailByOrderNumber(@PathVariable String orderNumber);
}
