package com.chasing.fan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chasing.fan.entity.Setmeal;
import com.chasing.fan.entity.SetmealDTO;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    void updateStatus(int status, List<Long> ids);
    List<Setmeal> listByCategoryId(Long categoryId);
}
