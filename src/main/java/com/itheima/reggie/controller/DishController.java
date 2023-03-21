package com.itheima.reggie.controller;


import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * description: 添加菜品
     *
     * @since: 1.0.0
     * @author: KangJiaMing
     * @date: 2023/3/20 20:39
     * @Param dishDto:
     * @return: com.itheima.reggie.common.R<java.lang.String>
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        System.out.println("dishDto = " + dishDto);
        dishService.saveWithFlavor(dishDto);
        return R.success("添加菜品成功");
    }


    /**
     * description: 查询菜品列表
     *
     * @since: 1.0.0
     * @author: KangJiaMing
     * @date: 2023/3/20 21:33
     * @Param page:
     * @Param pageSize:
     * @Param name:
     * @return: com.itheima.reggie.common.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page>
     */
//    @GetMapping("/page")
//    public R<PageInfo> page(int page, int pageSize, String name) {
//        PageInfo<Dish> dishPage = dishService.getPage(page,pageSize,name);
//        return R.success(dishPage);
//    }


}
