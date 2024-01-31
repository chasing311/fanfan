package com.chasing.fan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chasing.fan.common.CustomException;
import com.chasing.fan.entity.Category;
import com.chasing.fan.entity.Dish;
import com.chasing.fan.entity.Setmeal;
import com.chasing.fan.mapper.CategoryMapper;
import com.chasing.fan.service.CategoryService;
import com.chasing.fan.service.DishService;
import com.chasing.fan.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    DishService dishService;

    @Autowired
    SetmealService setmealService;

    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加dish查询条件，根据分类id进行查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int dishCount = dishService.count(dishLambdaQueryWrapper);
        log.info("dish查询条件，查询到的条目数为：{}", dishCount);
        //查看当前分类是否关联了菜品，如果已经关联，则抛出异常
        if (dishCount > 0){
            //已关联菜品，抛出一个业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加dish查询条件，根据分类id进行查询
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int setmealCount = setmealService.count(setmealLambdaQueryWrapper);
        //方便Debug用的
        log.info("setmeal查询条件，查询到的条目数为：{}", setmealCount);
        //查看当前分类是否关联了套餐，如果已经关联，则抛出异常
        if (setmealCount > 0){
            //已关联套餐，抛出一个业务异常
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }
        //正常删除
        super.removeById(id);
    }
}
