package com.yaoxiong.retail.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yaoxiong.retail.user.entity.User;



public interface UserService extends IService<User> {

    // Extract user information from the database
    User selectByUsername(String username);
}
