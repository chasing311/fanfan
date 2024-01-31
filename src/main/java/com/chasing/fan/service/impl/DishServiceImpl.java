package com.chasing.fan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chasing.fan.entity.Dish;
import com.chasing.fan.mapper.DishMapper;
import com.chasing.fan.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
