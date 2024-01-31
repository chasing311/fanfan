package com.chasing.fan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chasing.fan.entity.Setmeal;
import com.chasing.fan.mapper.SetmealMapper;
import com.chasing.fan.service.SetmealService;
import org.springframework.stereotype.Service;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
}
