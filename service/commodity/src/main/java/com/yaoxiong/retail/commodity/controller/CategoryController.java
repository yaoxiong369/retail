package com.yaoxiong.retail.commodity.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yaoxiong.retail.base.utils.Result;
import com.yaoxiong.retail.commodity.service.CategoryService;
import com.yaoxiong.retail.model.Commodity;
import com.yaoxiong.retail.vo.CommodityQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/config")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/category/list")
    public Result categoryList() {
        return categoryService.getCategoryListByCode(101);
    }

    @GetMapping("/unit/list")
    public Result unitList() {
        return categoryService.getCategoryListByCode(102);
    }

    @GetMapping("/location/list")
    public Result locationList() {
        return categoryService.getCategoryListByCode(103);
    }
}
