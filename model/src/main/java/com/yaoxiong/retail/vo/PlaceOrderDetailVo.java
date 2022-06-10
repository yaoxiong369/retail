package com.yaoxiong.retail.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "PlaceOrderDetailVo")
public class PlaceOrderDetailVo {
    private String picture;
    private String name;
    private Double sellPrice;
    private String unit;
    private Double quantity;
}
