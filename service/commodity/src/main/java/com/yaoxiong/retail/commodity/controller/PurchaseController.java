package com.yaoxiong.retail.commodity.controller;

import com.yaoxiong.retail.base.utils.Result;
import com.yaoxiong.retail.commodity.service.PurchaseService;
import com.yaoxiong.retail.model.Commodity;
import com.yaoxiong.retail.model.Purchase;
import com.yaoxiong.retail.vo.PurchaseQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/purchase")
@Slf4j
public class PurchaseController {

    @Autowired
    PurchaseService purchaseService;

    @PostMapping()
    public Result createPurchase(@Validated @RequestBody Purchase purchase){
        return purchaseService.savePurchase(purchase);
    }

    @GetMapping()
    public Result getPurchaseList(PurchaseQueryVo purchaseQueryVo){
        return purchaseService.getPurchaseList(purchaseQueryVo);
    }

    @PutMapping
    public Result updatePurchase(@Validated @RequestBody Purchase purchase) {
        return purchaseService.updataPurchase(purchase);
    }

}
