package com.chasing.fan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chasing.fan.common.Result;
import com.chasing.fan.entity.Category;
import com.chasing.fan.entity.DishDTO;
import com.chasing.fan.entity.Setmeal;
import com.chasing.fan.entity.SetmealDTO;
import com.chasing.fan.service.CategoryService;
import com.chasing.fan.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public Result<Page<SetmealDTO>> page(int page, int pageSize, String name) {
        //构造分页构造器对象
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDTO> setmealDTOPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo, queryWrapper);
        BeanUtils.copyProperties(pageInfo, setmealDTOPage, "records");

        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDTO> list = records.stream().map((item) -> {
            SetmealDTO setmealDto = new SetmealDTO();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            setmealDto.setCategoryName(category.getName());
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDTOPage.setRecords(list);
        return Result.success(setmealDTOPage);
    }

    @PostMapping("/add")
    public Result<String> saveWithDish(@RequestBody SetmealDTO setmealDto) {
        log.info("套餐信息：{}", setmealDto);
        setmealService.saveWithDish(setmealDto);
        return Result.success("套餐添加成功");
    }

    @GetMapping("/{id}")
    public Result<SetmealDTO> getWithDish(@PathVariable Long id) {
        SetmealDTO setmealDTO = setmealService.getWithDishById(id);
        log.info("查询到的数据为：{}", setmealDTO);
        return Result.success(setmealDTO);
    }

    @PostMapping("/edit")
    public Result<String> updateWithDish(@RequestBody SetmealDTO setmealDto) {
        log.info("套餐信息：{}", setmealDto);
        setmealService.updateWithDish(setmealDto);
        return Result.success("套餐添加成功");
    }
}
