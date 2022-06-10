package com.yaoxiong.retail.vo;

import com.yaoxiong.retail.model.Purchase;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
@ApiModel(description = "PurchaseInOrderVo")
public class PurchaseInOrderVo extends Purchase {
    @Min(value = 0, message = "sell volume must be greater than 0")
    private Double salesVolume;
}
