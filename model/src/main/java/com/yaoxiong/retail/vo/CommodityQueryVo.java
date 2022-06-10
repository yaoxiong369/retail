package com.yaoxiong.retail.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "CommodityQueryVo")
public class CommodityQueryVo {
    private String searchKeyWord;
    private String name;
    private Integer categoryId;
    private String remark;
    private int status;
}
