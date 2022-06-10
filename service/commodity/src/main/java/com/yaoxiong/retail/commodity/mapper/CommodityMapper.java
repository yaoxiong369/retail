package com.yaoxiong.retail.commodity.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yaoxiong.retail.model.Commodity;
import com.yaoxiong.retail.vo.CommodityWithOrder;
import com.yaoxiong.retail.vo.CommodityWithOrderQueryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommodityMapper extends BaseMapper<Commodity> {

    @Select("select commodity.*,if(purchase.id is null ,null,CONCAT('[',group_concat(JSON_OBJECT('id',purchase.id,'commodityId',purchase.commodity_id,'purchasePrice',purchase.purchase_price,\n" +
            "                                                       'sellPrice',purchase.sell_price,'purchaseQuantity',purchase.purchase_quantity,'remain',purchase.remain,'createDate',DATE_FORMAT(purchase.create_date,'%Y-%m-%d %H:%i:%S'))),']')) as purchases from commodity left join purchase on commodity.id = purchase.commodity_id where name like '%${commodityWithOrderQueryVo.keyword}%' group by commodity.id")//    ${(page.current-1)*page.size},${page.size}
    List<CommodityWithOrder> getCommodityWithOrderList(Page page, CommodityWithOrderQueryVo commodityWithOrderQueryVo);
}
