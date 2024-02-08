package com.chasing.fan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chasing.fan.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    List<Dish> listByCategoryId(Long categoryId);
    void updateStatus(int status, List<Long> ids);
    void removeByIdsIfStop(List<Long> ids);
}
