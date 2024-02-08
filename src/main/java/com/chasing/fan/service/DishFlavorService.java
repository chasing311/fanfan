package com.chasing.fan.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chasing.fan.entity.DishDTO;
import com.chasing.fan.entity.DishFlavor;

import java.util.List;

public interface DishFlavorService extends IService<DishFlavor> {
    void saveWithFlavor(DishDTO dishDTO);
    DishDTO getWithFlavorById(Long id);
    void updateWithFlavor(DishDTO dishDTO);
    Page<DishDTO> pageWithCategory(int page, int pageSize, String name);
    List<DishDTO> listWithFlavorsByCategoryId(Long categoryId);
}