package com.yaoxiong.retail.vo;

import com.yaoxiong.retail.model.Purchase;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@ApiModel(description = "OrderVo")
public class OrderVo {
    private Integer id;
    private Integer commodityId;
    private String orderNumber;
    @Size(max=50,message="The maximum length of the name is 50")
    private String name;
    @Min(value = 0, message = "sell price must be greater than 0")
    private Double sellPrice;
    private Double profit;
    private String unit;
    @Valid
    private List<PurchaseInOrderVo> purchases;
    @Min(value = 0, message = "sell price must be greater than 0")
    private Double quantity;
    @Size(max=300,message="The maximum length of the remarks is 300")
    private String remark;


}
