package com.yaoxiong.retail.commodity.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yaoxiong.retail.base.utils.Result;
import com.yaoxiong.retail.model.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {


    public Result getCategoryListByCode(Integer code);
}
