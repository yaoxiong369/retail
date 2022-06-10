package com.yaoxiong.retail.commodity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yaoxiong.retail.vo.CommodityWithOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommodityWithOrderMapper extends BaseMapper<CommodityWithOrder> {
    @Select("select commodity.*,JSON_EXTRACT(JSON_ARRAY(group_concat(JSON_OBJECT('id',purchase.id,'commodity_id',purchase.commodity_id,'purchase_price',purchase.purchase_price,\n" +
            "                                                       'sell_price',purchase.sell_price,'purchase_quantity',purchase.purchase_quantity,'remain',purchase.remain,'create_date',DATE_FORMAT(purchase.create_date,'%Y-%m-%d %H:%i:%S')))),'$') as purchases from commodity left join purchase on commodity.id = purchase.commodity_id group by commodity.id \n")
    List<CommodityWithOrder> getCommodityWithOrderList(Page page);


}
