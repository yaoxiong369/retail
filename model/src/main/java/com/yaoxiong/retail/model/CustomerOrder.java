package com.yaoxiong.retail.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.util.Date;

@Data
@ApiModel(description = "订单")
@Document("Order")
public class CustomerOrder {
    @TableId(type = IdType.AUTO)
    private String id;
    @Size(max=50,message="The maximum length of the name is 50")
    private String name;
    private String orderNumber;
    private String customerNumber;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "US/Eastern")
    private Date createDate;
    private Integer updataUserId;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "US/Eastern")
    private Date updateDate;
    @Size(max=300,message="The maximum length of the remarks is 300")
    private String remarks;
    private Integer status;

}
