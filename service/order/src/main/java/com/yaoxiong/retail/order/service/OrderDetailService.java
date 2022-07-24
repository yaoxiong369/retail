package com.yaoxiong.retail.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yaoxiong.retail.base.utils.Result;
import com.yaoxiong.retail.model.CustomerOrder;
import com.yaoxiong.retail.model.OrderDetail;
import com.yaoxiong.retail.vo.OrderVo;

public interface OrderDetailService extends IService<OrderDetail> {
    Result saveOrderDetail(OrderDetail orderDetail) throws Exception;

    Result saveOrderDetail(OrderVo orderVo) throws Exception;

    Result getOrderDetailByorderNumber(String orderNumber) throws Exception;

    Result getPlaceOrderDetailVo(String orderNumber) throws Exception;

    Result placeOrderDetail(CustomerOrder customerOrder) throws Exception;

    public void deleteOrderDetail(String orderNumber) throws Exception;

    public Result revenueDayAggregation(Integer intervalDay)  throws Exception;
}
