package com.yaoxiong.retail.order.service.impl;

import com.yaoxiong.retail.base.utils.Result;
import com.yaoxiong.retail.order.service.OrderDetailService;
import com.yaoxiong.retail.order.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Rollback
public class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Test
    public void orderDayAggregationTest() throws Exception {
        orderService.orderDayAggregation(30);
    }

    @Test
    public void getOrderTimeLineTest() throws Exception{
        orderService.getOrderTimeLine();
    }
}
