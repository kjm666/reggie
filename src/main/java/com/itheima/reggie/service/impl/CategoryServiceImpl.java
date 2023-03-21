package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;


    @Override
    public void deleteById(long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //设置删除条件
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        //看该分类关联多少菜品
        int count1 = dishService.count(dishLambdaQueryWrapper);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        //分类下有套餐不能删
        if (count1 > 0) {
            //抛出不能删除的自定义异常
            throw new CustomException("当前分类关联了菜品，不能删除");
        }
        if (count2 > 0) {
            //抛出不能删除的自定义异常
            throw new CustomException("当前分类关联了菜品，不能删除");
        }
        //删除类别
        super.removeById(id);
    }
}
