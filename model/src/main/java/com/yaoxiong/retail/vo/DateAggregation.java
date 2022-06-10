package com.yaoxiong.retail.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "DateAggregation")
public class DateAggregation {
    private String timeGranularity;
    private String indice;
}
