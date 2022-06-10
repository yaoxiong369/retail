package com.yaoxiong.retail.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yaoxiong.retail.model.CustomerOrder;
import com.yaoxiong.retail.vo.DateAggregation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper extends BaseMapper<CustomerOrder> {

    @Select("select count(*) as indice,DATE_FORMAT(create_date,'%Y-%m-%d') as time_granularity from order_detail where create_date>=DATE_SUB(CURDATE(),INTERVAL ${intervalDay} DAY) and create_date<=NOW() group by DATE_FORMAT(create_date,'%Y-%m-%d')")
    List<DateAggregation> orderAggregation(Integer intervalDay);
}
