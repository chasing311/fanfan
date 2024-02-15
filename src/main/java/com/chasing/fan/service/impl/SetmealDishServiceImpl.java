package com.chasing.fan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chasing.fan.common.CustomException;
import com.chasing.fan.entity.Category;
import com.chasing.fan.entity.Setmeal;
import com.chasing.fan.entity.SetmealDTO;
import com.chasing.fan.entity.SetmealDish;
import com.chasing.fan.mapper.SetmealDishMapper;
import com.chasing.fan.service.CategoryService;
import com.chasing.fan.service.SetmealDishService;
import com.chasing.fan.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
    @Autowired
    SetmealService setmealService;

    @Autowired
    CategoryService categoryService;

    @Override
    public void addWithDish(SetmealDTO setmealDTO) {
        setmealService.save(setmealDTO);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes = setmealDishes.stream().peek(item -> item.setSetmealId(setmealDTO.getId())).collect(Collectors.toList());
        this.saveBatch(setmealDishes);
    }

    @Override
    public void removeWithDish(List<Long> ids) {
        //先判断一下能不能删，如果status为1，则套餐在售，不能删
        LambdaQueryWrapper<Setmeal> setmealQueryWrapper = new LambdaQueryWrapper<>();
        setmealQueryWrapper.in(Setmeal::getId, ids);
        setmealQueryWrapper.eq(Setmeal::getStatus, 1);
        int count = setmealService.count(setmealQueryWrapper);
        if (count > 0) {
            throw new CustomException("套餐正在售卖中，请先停售再进行删除");
        }
        setmealService.removeByIds(ids);
        LambdaQueryWrapper<SetmealDish> setmealDishQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishQueryWrapper.in(SetmealDish::getSetmealId, ids);
        this.remove(setmealDishQueryWrapper);
    }

    @Override
    public void updateWithDish(SetmealDTO setmealDTO) {
        setmealService.updateById(setmealDTO);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealDTO.getId());
        this.remove(queryWrapper);

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes = setmealDishes.stream().peek(item -> item.setSetmealId(setmealDTO.getId())).collect(Collectors.toList());
        this.saveBatch(setmealDishes);
    }

    @Override
    public SetmealDTO getWithDishById(Long id) {
        Setmeal setmeal = setmealService.getById(id);
        SetmealDTO setmealDTO = new SetmealDTO();
        BeanUtils.copyProperties(setmeal, setmealDTO);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishes = this.list(queryWrapper);
        setmealDTO.setSetmealDishes(setmealDishes);
        return setmealDTO;
    }

    @Override
    public Page<SetmealDTO> pageWithDish(int page, int pageSize, String name) {
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        Page<SetmealDTO> setmealDTOPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(setmealPage, queryWrapper);
        BeanUtils.copyProperties(setmealPage, setmealDTOPage, "records");

        List<Setmeal> records = setmealPage.getRecords();

        List<SetmealDTO> list = records.stream().map((item) -> {
            SetmealDTO setmealDto = new SetmealDTO();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            setmealDto.setCategoryName(category.getName());
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDTOPage.setRecords(list);
        return setmealDTOPage;
    }

    @Override
    public List<SetmealDish> listBySetmealId(Long setmealId) {
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealId);
        return this.list(queryWrapper);

    }
}
