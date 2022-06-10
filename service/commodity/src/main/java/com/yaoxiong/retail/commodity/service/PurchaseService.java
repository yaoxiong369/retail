package com.yaoxiong.retail.commodity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yaoxiong.retail.base.utils.Result;
import com.yaoxiong.retail.model.Commodity;
import com.yaoxiong.retail.model.Purchase;
import com.yaoxiong.retail.vo.PurchaseQueryVo;

import java.util.List;

public interface PurchaseService extends IService<Purchase> {

    Result savePurchase(Purchase purchase);

    Result getPurchaseList(PurchaseQueryVo purchaseQueryVo);

    Result updataPurchase(Purchase purchase);

    List<Purchase> getRemainPurchaseList(Integer commodityId) throws Exception;
}
