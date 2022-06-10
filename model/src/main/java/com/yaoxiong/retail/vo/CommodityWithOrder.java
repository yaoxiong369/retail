package com.yaoxiong.retail.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.yaoxiong.retail.model.Commodity;
import com.yaoxiong.retail.model.Purchase;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@ApiModel(description = "CommodityWithOrder")
@Accessors(chain = true)
@TableName(autoResultMap = true)
public class CommodityWithOrder extends Commodity {
    private String purchases;
}
