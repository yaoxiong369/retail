package com.yaoxiong.retail.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ApiModel(description = "分类")
@Document("Category")
public class Category {
    private Integer id;
    private Integer parentId;
    private String name;
    private Integer categoryCode;
    private Integer level;
    private Float sortNumber;

}
