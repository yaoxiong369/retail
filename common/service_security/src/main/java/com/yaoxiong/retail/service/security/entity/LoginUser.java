package com.yaoxiong.retail.service.security.entity;

import com.yaoxiong.retail.service.security.entity.User;
import com.yaoxiong.retail.service.security.interceptor.JWTInterceptor;

public class LoginUser{
    public static User info(){
        return JWTInterceptor.userInfo.get();
    }
}
