package com.yaoxiong.retail.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yaoxiong.retail.base.utils.Result;
import com.yaoxiong.retail.enums.OrderStatusEnum;
import com.yaoxiong.retail.model.CustomerOrder;
import com.yaoxiong.retail.order.mapper.OrderMapper;
import com.yaoxiong.retail.order.service.OrderDetailService;
import com.yaoxiong.retail.order.service.OrderService;
import com.yaoxiong.retail.vo.DateAggregation;
import com.yaoxiong.retail.vo.OrderQueryVo;
import com.yaoxiong.retail.vo.TimeLineVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceimpl extends
        ServiceImpl<OrderMapper, CustomerOrder> implements OrderService {

    @Autowired
    OrderDetailService orderDetailService;

    @Override
    public Result getOrderList(Page<CustomerOrder> pageParam, OrderQueryVo orderQueryVo) throws Exception {
        if (pageParam == null || orderQueryVo == null || pageParam.getCurrent() < 0 || pageParam.getSize() <= 0) {
            return Result.error().message("Parameter error");
        }
        String keyWord = orderQueryVo.getKeyword();
        Date createDateRangeStart = orderQueryVo.getCreateDateRangeStart();
        Date createDateRangeEnd = orderQueryVo.getCreateDateRangeEnd();
        try {
            QueryWrapper<CustomerOrder> wrapper = new QueryWrapper<>();
            wrapper.ne("status", OrderStatusEnum.DELETE.getStatus()).orderByDesc("create_date");
            if (!StringUtils.isEmpty(keyWord)) {
                wrapper.and(wr -> wr.like("name", keyWord)
                        .or().like("remarks", keyWord));
            }
            if (createDateRangeStart != null && createDateRangeEnd != null &&
                    createDateRangeEnd.getTime() > createDateRangeStart.getTime()) {
                wrapper.gt("create_date", createDateRangeStart)
                        .le("create_date", createDateRangeEnd);
            }
            //调用mapper的方法
            IPage<CustomerOrder> pages = baseMapper.selectPage(pageParam, wrapper);

            return Result.ok().data("object", pages.getRecords())
                    .data("total", pages.getTotal());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error().message("error：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result deleteOrder(String orderNumber) throws Exception {
        if (StringUtils.isEmpty(orderNumber)) {
            throw new Exception("Parameter error");
        }

        try {
            QueryWrapper<CustomerOrder> wrapper = new QueryWrapper<>();
            wrapper.eq("order_number", orderNumber);

            CustomerOrder customerOrder = baseMapper.selectOne(wrapper);
            if (customerOrder != null && customerOrder.getStatus().intValue() == OrderStatusEnum.FINISH.getStatus().intValue()) {
                return Result.error().message("The order has been completed. Cannot delete");
            }

            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("order_number", orderNumber);
            updateWrapper.set("status", OrderStatusEnum.DELETE.getStatus());
            baseMapper.update(customerOrder, updateWrapper);
            orderDetailService.deleteOrderDetail(orderNumber);
            return Result.ok().message("ok!");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error().message("error：" + e.getMessage());
        }

    }

    @Override
    public Result finisheOrder(String orderNumber) throws Exception {
        if (StringUtils.isEmpty(orderNumber)) {
            throw new Exception("Parameter error");
        }

        try {
            QueryWrapper<CustomerOrder> wrapper = new QueryWrapper<>();
            wrapper.eq("order_number", orderNumber);

            CustomerOrder customerOrder = baseMapper.selectOne(wrapper);
            if (customerOrder != null && customerOrder.getStatus().intValue() == OrderStatusEnum.DELETE.getStatus().intValue()) {
                return Result.error().message("The order has been deleted. Unable to complete operation");
            }

            UpdateWrapper updateWrapper = new UpdateWrapper();
            updateWrapper.eq("order_number", orderNumber);
            updateWrapper.set("status", OrderStatusEnum.FINISH.getStatus());
            customerOrder.setUpdateDate(new Date());
            baseMapper.update(customerOrder, updateWrapper);
            return Result.ok().message("ok!");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error().message("error：" + e.getMessage());
        }
    }

    public void saveOrder(String orderNumber, String remarks, String customerNumber) throws Exception {
        if (StringUtils.isEmpty(orderNumber)) {
            throw new Exception("Missing OrderNumber parameter when creating order");
        }
        QueryWrapper<CustomerOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("order_number", orderNumber);

        CustomerOrder customerOrder = baseMapper.selectOne(wrapper);

        if (customerOrder != null) {
            customerOrder.setRemarks(remarks);
            customerOrder.setCustomerNumber(customerNumber);
            customerOrder.setUpdateDate(new Date());
            baseMapper.updateById(customerOrder);
        } else {
            customerOrder = new CustomerOrder();
            customerOrder.setOrderNumber(orderNumber);
            customerOrder.setRemarks(remarks);
            customerOrder.setCustomerNumber(customerNumber);
            customerOrder.setName(new SimpleDateFormat("yyyy_MM_dd").format(new Date()) + "_" + String.format("%08d", new Date().getTime()) + "_" + customerNumber);
            customerOrder.setCreateDate(new Date());
            customerOrder.setUpdateDate(new Date());
            customerOrder.setStatus(OrderStatusEnum.CREATE.getStatus());
            baseMapper.insert(customerOrder);
        }
    }

    @Override
    public Result orderDayAggregation(Integer intervalDay) throws Exception {
        if (intervalDay == null || intervalDay.intValue() < 0) {
            return Result.error().message("Parameter error");
        }
        try {
            List<DateAggregation> aggregationList = baseMapper.orderAggregation(intervalDay);
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

    public Result getOrderTimeLine() throws Exception {
        List<TimeLineVo> reList = new LinkedList<>();
        try {
            QueryWrapper<CustomerOrder> wrapper = new QueryWrapper<>();
            wrapper.ne("status", OrderStatusEnum.DELETE.getStatus())
                    .isNotNull("update_date")
                    .orderByDesc("update_date");

            //调用mapper的方法
            IPage<CustomerOrder> pages = baseMapper.selectPage(new Page<>(1, 10), wrapper);
            for (CustomerOrder c : pages.getRecords()) {
                TimeLineVo timeLineVo = new TimeLineVo();
                BeanUtils.copyProperties(timeLineVo, c);
                timeLineVo.setStatus(OrderStatusEnum.getComment(Integer.valueOf(timeLineVo.getStatus())));
                reList.add(timeLineVo);
            }

            return Result.ok().data("object", reList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error().message("error：" + e.getMessage());
        }
    }

}
