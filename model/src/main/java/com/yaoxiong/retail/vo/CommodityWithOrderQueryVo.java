package com.yaoxiong.retail.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoxiong.retail.model.Commodity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
//@ApiModel(description = "CommodityWithOrderQueryVo")
public class CommodityWithOrderQueryVo {
    private String orderNumber;
    private String keyword;
}
