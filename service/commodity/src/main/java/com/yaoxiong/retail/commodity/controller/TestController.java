package com.yaoxiong.retail.commodity.controller;

import com.yaoxiong.retail.base.utils.Result;

//import com.yaoxiong.retail.service.security.entity.LoginUser;
//import com.yaoxiong.retail.service.security.entity.LoginUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/commodity/test")
public class TestController {
    @GetMapping("/info")
    public Result test() {
        return Result.ok().message("test ok");
//        return Result.ok().data("children", LoginUser.info().getUsername());
    }
}
