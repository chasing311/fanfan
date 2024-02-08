package com.chasing.fan.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chasing.fan.entity.SetmealDTO;
import com.chasing.fan.entity.SetmealDish;

import java.util.List;

public interface SetmealDishService extends IService<SetmealDish> {
    void addWithDish(SetmealDTO setmealDTO);
    void removeWithDish(List<Long> ids);
    void updateWithDish(SetmealDTO setmealDTO);
    SetmealDTO getWithDishById(Long id);
    Page<SetmealDTO> pageWithDish(int page, int pageSize, String name);
    List<SetmealDish> listBySetmealId(Long setmealId);
}
