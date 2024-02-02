package com.chasing.fan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chasing.fan.entity.Setmeal;
import com.chasing.fan.entity.SetmealDTO;
import com.chasing.fan.entity.SetmealDish;
import com.chasing.fan.mapper.SetmealMapper;
import com.chasing.fan.service.SetmealDishService;
import com.chasing.fan.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;
    @Override
    public void saveWithDish(SetmealDTO setmealDTO) {
        this.save(setmealDTO);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes = setmealDishes.stream().peek(item -> item.setSetmealId(setmealDTO.getId())).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    @Override
    public SetmealDTO getWithDishById(Long id) {
        Setmeal setmeal = this.getById(id);
        SetmealDTO setmealDTO = new SetmealDTO();
        BeanUtils.copyProperties(setmeal, setmealDTO);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);
        setmealDTO.setSetmealDishes(setmealDishes);
        return setmealDTO;
    }

    @Override
    public void updateWithDish(SetmealDTO setmealDTO) {
        this.updateById(setmealDTO);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealDTO.getId());
        setmealDishService.remove(queryWrapper);

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes = setmealDishes.stream().peek(item -> item.setSetmealId(setmealDTO.getId())).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }
}
