package com.yaoxiong.retail.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@ApiModel(description = "商品")
@Document("Commodity")
@TableName(value = "commodity",autoResultMap = true)
public class Commodity {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("category_id")
    private Integer categoryId;

    @Size(max=50,message="The maximum length of the name is 50")
    @TableField("name")
    private String name;

    @Min(value = 0, message = "年龄必须大于10")
    @TableField("remain")
    private Double remain;

    @TableField("picture")
    private String picture;

    @TableField("unit")
    private Integer unit;

    @TableField("category_name")
    private String categoryName;

    @Size(max=300,message="The maximum length of the remarks is 300")
    @TableField("remarks")
    private String remarks;

    @TableField("status")
    private int status;


}
