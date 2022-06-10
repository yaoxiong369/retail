package com.yaoxiong.retail.order.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yaoxiong.retail.base.utils.Result;
import com.yaoxiong.retail.model.CustomerOrder;
import com.yaoxiong.retail.order.service.OrderDetailService;
import com.yaoxiong.retail.order.service.OrderService;
import com.yaoxiong.retail.vo.OrderQueryVo;
import com.yaoxiong.retail.vo.OrderVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/order")
@Slf4j
public class OrderController {

    @Autowired
    OrderDetailService orderDetailService;

    @Autowired
    OrderService orderService;


    @PostMapping("/detail")
    public Result createOrderDetail(@Validated @RequestBody OrderVo orderVo) throws Exception {
        return orderDetailService.saveOrderDetail(orderVo);
    }

    @GetMapping("/detail/{orderNumber}")
    public Result getOrderDetailByOrderNumber(@PathVariable String orderNumber) throws Exception {
        return orderDetailService.getOrderDetailByorderNumber(orderNumber);
    }

    @GetMapping("/place/{orderNumber}")
    public Result getPlaceOrderDetailVo(@PathVariable String orderNumber) throws Exception {
        return orderDetailService.getPlaceOrderDetailVo(orderNumber);
    }

    @PostMapping("/place")
    public Result placeOrderDetail(@Validated @RequestBody CustomerOrder customerOrder) throws Exception {
        return orderDetailService.placeOrderDetail(customerOrder);
    }

    @GetMapping("/{page}/{pageSize}")
    public Result getOrderList(@PathVariable Long page,
                               @PathVariable Long pageSize,
                               OrderQueryVo orderQueryVo)  throws Exception {
        Page<CustomerOrder> pageParam = new Page<>(page, pageSize);
        return orderService.getOrderList(pageParam,orderQueryVo);
    }

    @DeleteMapping(path = "/delete/{orderNumber}")
    public Result deletOrder(@PathVariable String orderNumber)  throws Exception {
        return orderService.deleteOrder(orderNumber);
    }

    @PostMapping(path = "/finish/{orderNumber}")
    public Result finishOrder(@PathVariable String orderNumber)  throws Exception {
        return orderService.finisheOrder(orderNumber);
    }

    @GetMapping(path="/aggregation/{intervalDay}")
    public Result orderDayAggregation(@PathVariable Integer intervalDay)  throws Exception {
        return orderService.orderDayAggregation(intervalDay);
    }

    @GetMapping(path="/revenue/aggregation/{intervalDay}")
    public Result revenueDayAggregation(@PathVariable Integer intervalDay)  throws Exception {
        return orderDetailService.revenueDayAggregation(intervalDay);
    }

    @GetMapping(path="/timeline")
    public Result getOrderTimeLine()  throws Exception {
        return orderService.getOrderTimeLine();
    }

}
