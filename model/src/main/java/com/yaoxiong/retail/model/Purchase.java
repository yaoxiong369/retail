package com.yaoxiong.retail.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@ApiModel(description = "采购")
@Document("Purchase")
@TableName(value = "purchase", autoResultMap = true)
public class Purchase {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer commodityId;
    @Min(value = 0, message = "purchase price must be greater than or equal to 0")
    private Double purchasePrice;
    @Min(value = 0, message = "sell price must be greater than or equal to 0")
    private Double sellPrice;
    @Min(value = 0, message = "vip price must be greater than or equal to 0")
    private Double vipPrice;
    private String unit;
    @Min(value = 0, message = "quantity must be greater than or equal to 0")
    private Double purchaseQuantity;
    private Double remain;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expirationDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyDate;
    private Integer modifyUserId;
    @Size(max=300,message="The maximum length of the remarks is 300")
    private String remarks;
    private int status;
    private String storageLocation;


}
