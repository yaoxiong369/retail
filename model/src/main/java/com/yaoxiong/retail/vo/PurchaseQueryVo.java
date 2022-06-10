package com.yaoxiong.retail.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "PurchaseQueryVo")
public class PurchaseQueryVo {
    private Integer commodityId;
    private Date createDateRangeStart;
    private Date createDateRangeEnd;
}
