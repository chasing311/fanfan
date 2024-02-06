package com.chasing.fan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chasing.fan.entity.Dish;
import com.chasing.fan.entity.DishDTO;
import com.chasing.fan.entity.DishFlavor;
import com.chasing.fan.mapper.DishFlavorMapper;
import com.chasing.fan.service.DishFlavorService;
import com.chasing.fan.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
    @Autowired
    private DishService dishService;
    @Override
    public void saveWithFlavor(DishDTO dishDTO) {
        dishService.save(dishDTO);
        Long dishId = dishDTO.getId();
        //将获取到的dishId赋值给dishFlavor的dishId属性
        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors = flavors.stream().peek(item-> item.setDishId(dishId)).collect(Collectors.toList());
        //同时将菜品口味数据保存到dish_flavor表
        this.saveBatch(flavors);
    }

    @Override
    public DishDTO getWithFlavorById(Long id) {
        Dish dish = dishService.getById(id);
        DishDTO dishDTO = new DishDTO();
        BeanUtils.copyProperties(dish, dishDTO);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> dishFlavors = this.list(queryWrapper);
        dishDTO.setFlavors(dishFlavors);
        return dishDTO;
    }

    @Override
    public void updateWithFlavor(DishDTO dishDTO) {
        dishService.updateById(dishDTO);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDTO.getId());
        this.remove(queryWrapper);
        Long dishId = dishDTO.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors = flavors.stream().peek(item -> item.setDishId(dishId)).collect(Collectors.toList());
        this.saveBatch(flavors);
    }
}
