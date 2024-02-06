package com.chasing.fan.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chasing.fan.common.Result;
import com.chasing.fan.entity.SetmealDTO;
import com.chasing.fan.service.SetmealDishService;
import com.chasing.fan.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;

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
