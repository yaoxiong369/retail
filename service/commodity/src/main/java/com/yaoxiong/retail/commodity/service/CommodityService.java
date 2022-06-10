package com.yaoxiong.retail.commodity.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yaoxiong.retail.base.utils.Result;
import com.yaoxiong.retail.model.Commodity;
import com.yaoxiong.retail.model.OrderDetail;
import com.yaoxiong.retail.vo.CommodityQueryVo;
import com.yaoxiong.retail.vo.CommodityWithOrder;
import com.yaoxiong.retail.vo.CommodityWithOrderQueryVo;
import com.yaoxiong.retail.vo.OrderVo;

import java.util.List;

public interface CommodityService  extends IService<Commodity> {
    Result getCommodityById(Integer id);

    Result saveCommodity(Commodity commodity);

    Result updataCommodity(Commodity commodity);

    Result selectCommodityPage(Page<Commodity> pageParam, CommodityQueryVo commodityQueryVo);

    Result getCommodityWithOrder(Page<CommodityWithOrder> pageParam, CommodityWithOrderQueryVo commodityWithOrderQueryVo);

    Result updateCommodityAndPurchaseRemain(OrderVo orderVo) throws Exception;
}
