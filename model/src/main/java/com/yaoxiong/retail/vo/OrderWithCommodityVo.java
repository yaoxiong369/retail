package com.yaoxiong.retail.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoxiong.retail.model.Commodity;
import com.yaoxiong.retail.model.OrderDetail;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@ApiModel(description = "OrderWithCommodity")
@Accessors(chain = true)
@TableName(autoResultMap = true)
public class OrderWithCommodityVo extends OrderDetail {
    private Commodity commodity;
}
