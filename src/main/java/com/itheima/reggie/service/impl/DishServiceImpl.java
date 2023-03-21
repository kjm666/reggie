package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.DishFlavorMapper;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;


    /**
     * description: 新增菜品表，同时向口味表添加
     *
     * @since: 1.0.0
     * @author: KangJiaMing
     * @date: 2023/3/20 20:48
     * @Param dishDto:
     * @return: void
     */
    @Transactional//多张表操作，涉及到事务
    @Override
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);
        Long id = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(id);
            dishFlavorService.save(flavor);
        }
    }

//    @Override
//    public PageInfo<Dish> getPage(int page, int pageSize, String name) {
//        PageHelper.startPage(page, pageSize);
//        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        dishLambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
//        dishLambdaQueryWrapper.like(Dish::getName, name);
//        List<Dish> dishes = dishMapper.selectList(dishLambdaQueryWrapper);
//        for (Dish dish : dishes) {
//            String categoryName = dishFlavorMapper.findById(dish.getCategoryId());
//            dish.setCategoryName(categoryName);
//        }
//        PageInfo<Dish> dishPageInfo = new PageInfo<>(dishes);
//        System.out.println("dishPageInfo = " + dishPageInfo);
//        return dishPageInfo;
//    }
}
