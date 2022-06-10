package com.yaoxiong.retail.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yaoxiong.retail.base.utils.Result;
import com.yaoxiong.retail.model.CustomerOrder;
import com.yaoxiong.retail.vo.OrderQueryVo;

public interface OrderService {
    Result getOrderList(Page<CustomerOrder> pageParam, OrderQueryVo orderQueryVo) throws Exception;

    Result deleteOrder(String orderNumber) throws Exception;

    Result finisheOrder(String orderNumber) throws Exception;

    public void saveOrder(String orderNumber, String remarks, String customerNumber) throws Exception;

    public Result orderDayAggregation(Integer intervalDay)  throws Exception;

    public Result getOrderTimeLine() throws Exception;
}
