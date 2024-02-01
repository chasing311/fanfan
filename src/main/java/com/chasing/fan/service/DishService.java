package com.chasing.fan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chasing.fan.entity.Dish;
import com.chasing.fan.entity.DishDTO;

public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDTO dishDTO);
    DishDTO getWithFlavorById(Long id);

    void updateWithFlavor(DishDTO dishDTO);
}
