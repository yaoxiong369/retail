package com.yaoxiong.retail.order.controller;

import com.yaoxiong.retail.base.utils.Result;

//import com.yaoxiong.retail.service.security.entity.LoginUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/test/v1")
public class TestController {
    @GetMapping("/info")
    public Result test(String username, HttpServletRequest httpServletRequest) {
        return null;
//        return Result.ok().data("children", LoginUser.info().getUsername());
    }
}
