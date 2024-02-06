package com.chasing.fan.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chasing.fan.entity.Dish;
import com.chasing.fan.entity.DishDTO;

import java.util.List;

public interface DishService extends IService<Dish> {
    List<Dish> listByCategoryId(Long categoryId);
    Page<DishDTO> pageWithCategory(int page, int pageSize, String name);
    void updateStatus(int status, List<Long> ids);
    void removeByIdsIfStop(List<Long> ids);
}
