package com.chasing.fan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chasing.fan.entity.Setmeal;
import com.chasing.fan.entity.SetmealDTO;

public interface SetmealService extends IService<Setmeal> {
    void saveWithDish(SetmealDTO setmealDTO);
    SetmealDTO getWithDishById(Long id);
    void updateWithDish(SetmealDTO setmealDTO);
}
