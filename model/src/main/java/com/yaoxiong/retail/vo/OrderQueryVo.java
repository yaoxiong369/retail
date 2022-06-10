package com.yaoxiong.retail.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@ApiModel(description = "OrderQueryVo")
public class OrderQueryVo {
    private String keyword;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDateRangeStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDateRangeEnd;
}
