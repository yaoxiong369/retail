package com.yaoxiong.retail.order.service.impl;

import com.yaoxiong.retail.base.utils.Result;
import com.yaoxiong.retail.order.service.OrderDetailService;
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
public class OrderDetailServiceTest {
    @Autowired
    OrderDetailService orderDetailService;

    @Test
    public void getPlaceOrderDetailVoTest() throws Exception {
        Result result =  orderDetailService.getPlaceOrderDetailVo("dfd178-c7be-12b2-0078-dc41d8cc1");
        System.out.println(result);
    }

}
