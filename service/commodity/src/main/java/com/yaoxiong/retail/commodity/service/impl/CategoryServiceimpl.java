package com.yaoxiong.retail.commodity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yaoxiong.retail.base.utils.Result;
import com.yaoxiong.retail.commodity.mapper.CategoryMapper;
import com.yaoxiong.retail.commodity.service.CategoryService;
import com.yaoxiong.retail.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceimpl  extends
        ServiceImpl<CategoryMapper, Category> implements CategoryService {


    @Override
    public Result getCategoryListByCode(Integer code) {
        try{
            QueryWrapper<Category> wrapper = new QueryWrapper<>();
            wrapper.eq("category_code",code);
            List<Category> patientList = baseMapper.selectList(wrapper);
            return Result.ok().data("list",patientList);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return Result.error().message("error:"+e.getMessage());
        }

    }
}
