package com.yaoxiong.retail.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Data
@ApiModel(description = "订单详情")
@Document("OrderDetail")
public class OrderDetail {
    private Integer id;
    private Integer commodityId;
    private String orderNumber;
    private String name;
    private Double sellPrice;
    private Double profit;
    private String unit;
    private String purchases;
    private Double quantity;
    private String remarks;
    private Date createDate;
    private Integer createUserId;
    private Date modifyDate;
    private Integer modifyUserId;
    private int status;
}
