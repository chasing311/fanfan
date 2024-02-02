package com.chasing.fan.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chasing.fan.common.Result;
import com.chasing.fan.entity.Category;
import com.chasing.fan.entity.Dish;
import com.chasing.fan.entity.DishDTO;
import com.chasing.fan.entity.Setmeal;
import com.chasing.fan.service.SetmealService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {
}
