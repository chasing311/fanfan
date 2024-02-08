package com.chasing.fan.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chasing.fan.common.Result;
import com.chasing.fan.entity.*;
import com.chasing.fan.service.DishService;
import com.chasing.fan.service.SetmealDishService;
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
    private SetmealDishService setmealDishService;
    @Autowired
    private DishService dishService;

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result<Page<SetmealDTO>> page(int page, int pageSize, String name) {
        log.info("套餐分页查询：page={}, pageSize={}, name={}", page, pageSize, name);
        Page<SetmealDTO> pageInfo = setmealDishService.pageWithDish(page, pageSize, name);
        return Result.success(pageInfo);
    }

    /**
     * 套餐列表查询
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public Result<List<Setmeal>> list(Long categoryId) {
        log.info("查询套餐列表：{}", categoryId);
        List<Setmeal> setmealList = setmealService.listByCategoryId(categoryId);
        return Result.success(setmealList);
    }

    /**
     * 套餐详情查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<SetmealDTO> getWithDish(@PathVariable Long id) {
        SetmealDTO setmealDTO = setmealDishService.getWithDishById(id);
        log.info("查询套餐信息：{}", setmealDTO);
        return Result.success(setmealDTO);
    }

    /**
     * 套餐菜品详情查询
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    public Result<List<DishDTO>> getSetmealDishes(@PathVariable Long id) {
        List<SetmealDish> setmealDishList = setmealDishService.listBySetmealId(id);
        List<DishDTO> dishDTOList = setmealDishList.stream().map(item -> {
            DishDTO dishDTO = new DishDTO();
            BeanUtils.copyProperties(item, dishDTO);
            Long dishId = item.getDishId();
            Dish dish = dishService.getById(dishId);
            BeanUtils.copyProperties(dish, dishDTO);
            return dishDTO;
        }).collect(Collectors.toList());
        return Result.success(dishDTOList);
    }

    /**
     * 添加套餐
     * @param setmealDto
     * @return
     */
    @PostMapping("/add")
    public Result<String> addWithDish(@RequestBody SetmealDTO setmealDto) {
        log.info("添加套餐信息：{}", setmealDto);
        setmealDishService.addWithDish(setmealDto);
        return Result.success("套餐添加成功");
    }

    /**
     * 更新套餐
     * @param setmealDto
     * @return
     */
    @PostMapping("/edit")
    public Result<String> updateWithDish(@RequestBody SetmealDTO setmealDto) {
        log.info("更新套餐信息：{}", setmealDto);
        setmealDishService.updateWithDish(setmealDto);
        return Result.success("套餐添加成功");
    }

    /**
     * 启售/停售套餐（批量）
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<String> updateStatus(@PathVariable Integer status, @RequestParam List<Long> ids) {
        if (status == 1) {
            log.info("启售套餐id：{}", ids);
        } else {
            log.info("停售套餐id：{}", ids);
        }
        setmealService.updateStatus(status, ids);
        return Result.success(status == 1 ? "启售成功" : "停售成功");
    }

    /**
     * 删除套餐（批量）
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> deleteByIds(@RequestParam List<Long> ids) {
        log.info("删除套餐id：{}",ids);
        setmealDishService.removeWithDish(ids);
        return Result.success("删除成功");
    }
}
