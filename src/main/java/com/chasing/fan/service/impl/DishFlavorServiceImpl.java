package com.chasing.fan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chasing.fan.entity.Category;
import com.chasing.fan.entity.Dish;
import com.chasing.fan.entity.DishDTO;
import com.chasing.fan.entity.DishFlavor;
import com.chasing.fan.mapper.DishFlavorMapper;
import com.chasing.fan.service.CategoryService;
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
    @Autowired
    private CategoryService categoryService;

    @Override
    public void saveWithFlavor(DishDTO dishDTO) {
        dishService.save(dishDTO);
        Long dishId = dishDTO.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        flavors = flavors.stream().peek(item-> item.setDishId(dishId)).collect(Collectors.toList());
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

    @Override
    public Page<DishDTO> pageWithCategory(int page, int pageSize, String name) {
        Page<Dish> dishPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(dishPage, queryWrapper);

        Page<DishDTO> dishDTOPage = new Page<>(page, pageSize);
        BeanUtils.copyProperties(dishPage, dishDTOPage, "records");

        List<Dish> dishList = dishPage.getRecords();

        List<DishDTO> dishDTOList = dishList.stream().map((item) -> {
            DishDTO dishDTO = new DishDTO();
            BeanUtils.copyProperties(item, dishDTO);
            Long categoryId = item.getCategoryId();  //分类id
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDTO.setCategoryName(categoryName);
            return dishDTO;
        }).collect(Collectors.toList());

        dishDTOPage.setRecords(dishDTOList);
        return dishDTOPage;
    }

    @Override
    public List<DishDTO> listWithFlavorsByCategoryId(Long categoryId) {
        List<Dish> list = dishService.listByCategoryId(categoryId);
        List<DishDTO> dishDTOList = list.stream().map((item) -> {
            DishDTO dishDTO = new DishDTO();
            BeanUtils.copyProperties(item, dishDTO);
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                dishDTO.setCategoryName(category.getName());
            }
            Long itemId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(itemId != null, DishFlavor::getDishId, itemId);
            List<DishFlavor> flavors = this.list(lambdaQueryWrapper);
            dishDTO.setFlavors(flavors);
            return dishDTO;
        }).collect(Collectors.toList());
        return dishDTOList;
    }
}
