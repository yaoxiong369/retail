package com.yaoxiong.retail.commodity.client;

import com.yaoxiong.retail.base.utils.Result;
import com.yaoxiong.retail.model.OrderDetail;
import com.yaoxiong.retail.vo.OrderVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "commodity-server")
@Repository
public interface CommodityFeignClient {

    @PutMapping("/api/commodity/inner/updateCommodityAndPurchaseRemain")
    public Result updateCommodityAndPurchaseRemain(@RequestBody OrderVo orderVo);


}
