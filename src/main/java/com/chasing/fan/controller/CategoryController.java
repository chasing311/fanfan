package com.chasing.fan.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chasing.fan.common.Result;
import com.chasing.fan.entity.Category;
import com.chasing.fan.service.CategoryService;
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
     * 添加分类
     * @param category
     * @return
     */
    @PostMapping("/add")
    public Result<String> save(@RequestBody Category category) {
        log.info("添加分类信息: {}", category);
        categoryService.save(category);
        return Result.success(category.getType() == 1 ? "添加菜品分类成功！" : "添加套餐分类成功！");
    }

    /**
     * 修改分类
     * @param category
     * @return
     */
    @PostMapping("/edit")
    public Result<String> update(@RequestBody Category category) {
        log.info("修改分类信息: {}", category);
        categoryService.updateById(category);
        return Result.success(category.getType() == 1 ? "修改菜品分类成功！" : "修改套餐分类成功！");
    }

    /**
     * 分类分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public Result<Page<Category>> page(int page, int pageSize, String type) {
        log.info("分类分页查询, page={}, pageSize={}, type={}", page, pageSize, type);
        Page<Category> pageInfo = categoryService.pageWithType(page, pageSize, type);
        return Result.success(pageInfo);
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    private Result<String> delete(Long id) {
        log.info("删除分类的id：{}", id);
        categoryService.remove(id);
        return Result.success("分类信息删除成功");
    }

    /**
     * 分类列表查询
     * @param category
     * @return
     */
    @GetMapping("/list")
    public Result<List<Category>> list(Category category) {
        log.info("分类列表查询, type={}", category.getType());
        List<Category> list = categoryService.listByType(category.getType());
        return Result.success(list);
    }
}
