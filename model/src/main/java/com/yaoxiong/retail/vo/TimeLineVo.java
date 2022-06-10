package com.yaoxiong.retail.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@ApiModel(description = "TimeLineVo")
public class TimeLineVo {
    private String updateDate;
    private String Status;
    private String OrderNumber;
}
