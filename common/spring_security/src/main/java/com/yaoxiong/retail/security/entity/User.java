package com.yaoxiong.retail.security.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "User entity class")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "username")
    private String username;

    @ApiModelProperty(value = "password")
    private String password;

    @ApiModelProperty(value = "nickName")
    private String nickName;

    @ApiModelProperty(value = "salt")
    private String salt;

    @ApiModelProperty(value = "token")
    private String token;

}

