package com.yaoxiong.retail.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yaoxiong.retail.vo.DateAggregation;
import com.yaoxiong.retail.vo.OrderWithCommodityVo;
import com.yaoxiong.retail.model.Commodity;
import com.yaoxiong.retail.model.OrderDetail;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
    @Select("select order_detail.* from order_detail where order_detail.order_number = '${orderNumber}'")
    @Results({
            @Result(property = "commodity", javaType = Commodity.class, column = "commodity_id"
                    , one = @One(select = "com.yaoxiong.retail.order.mapper.OrderDetailMapper.getCommodityById"))
    })
    List<OrderWithCommodityVo> getOrderWithCommodityList(String orderNumber);

    @Select("select * from commodity where id = ${id}")
    Commodity getCommodityById(String id);

    @Select("select sum(quantity*order_detail.sell_price) as indice,DATE_FORMAT(create_date,'%Y-%m-%d') as time_granularity  from order_detail  where create_date>=DATE_SUB(CURDATE(),INTERVAL ${intervalDay} DAY) and create_date<=NOW() and status=1 group by DATE_FORMAT(create_date,'%Y-%m-%d')")
    List<DateAggregation> revenueAggregation(Integer intervalDay);
}
