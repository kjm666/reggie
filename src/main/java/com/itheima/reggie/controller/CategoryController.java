package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * description: 添加分类
     *
     * @since: 1.0.0
     * @author: KangJiaMing
     * @date: 2023/3/15 20:59
     * @Param category:
     * @return: com.itheima.reggie.common.R<java.lang.String>
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        categoryService.save(category);
        return R.success("添加成功分类");
    }

    /**
     * description: 分页查询
     *
     * @since: 1.0.0
     * @author: KangJiaMing         SB李季峰
     * @date: 2023/3/15 21:22
     * @Param page:
     * @Param pageSize:
     * @return: com.itheima.reggie.common.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page>
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        //分页构造器
        Page<Category> pageInfo = new Page(page, pageSize);
        //条件过滤器，排序
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.orderByAsc(Category::getSort);
        //进行分页查询
        categoryService.page(pageInfo, lqw);
        return R.success(pageInfo);
    }


    /**
     * description: 根据id删除类别
     *
     * @since: 1.0.0
     * @author: KangJiaMing
     * @date: 2023/3/16 19:20
     * @Param ids:
     * @return: com.itheima.reggie.common.R<java.lang.String>
     */
    @DeleteMapping
    public R<String> delete(long ids) {
        log.info("删除类别的id:" + ids);
        categoryService.deleteById(ids);
        return R.success("分类删除成功");
    }


    /**
     * description: 修改分类
     *
     * @since: 1.0.0
     * @author: KangJiaMing
     * @date: 2023/3/16 19:58
     * @Param category:
     * @return: com.itheima.reggie.common.R<java.lang.String>
     */
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        log.info("类别修改为" + category);
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(Category::getId, category.getId());
        categoryService.update(category, categoryLambdaQueryWrapper);
        return R.success("修改分类成功");
    }


    /**
     * description:根据条件查询分类
     *
     * @since: 1.0.0
     * @author: KangJiaMing
     * @date: 2023/3/20 20:16
     * @Param category:
     * @return: com.itheima.reggie.common.R<java.util.List < com.itheima.reggie.entity.Category>>
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        categoryLambdaQueryWrapper.orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(categoryLambdaQueryWrapper);
        return R.success(list);
    }

}
