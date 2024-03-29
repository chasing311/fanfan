package com.chasing.fan.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chasing.fan.common.Result;
import com.chasing.fan.entity.Dish;
import com.chasing.fan.entity.DishDTO;
import com.chasing.fan.service.DishFlavorService;
import com.chasing.fan.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Resource
    private RedisTemplate<String, List<DishDTO>> redisTemplate;

    private String cacheKey(Dish dish) {
        return "dish_" + dish.getCategoryId();
    }
    /**
     * 菜品分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result<Page<DishDTO>> page(int page, int pageSize, String name) {
        log.info("菜品分页查询, page={}, pageSize={}, type={}", page, pageSize, name);
        Page<DishDTO> dishDTOPage = dishFlavorService.pageWithCategory(page, pageSize, name);
        return Result.success(dishDTOPage);
    }


    /**
     * 菜品查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<DishDTO> getWithFlavor(@PathVariable Long id) {
        log.info("菜品查询, id={}", id);
        DishDTO dishDTO = dishFlavorService.getWithFlavorById(id);
        return Result.success(dishDTO);
    }

    /**
     * 菜品列表查询
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public Result<List<DishDTO>> get(Dish dish) {
        log.info("菜品列表查询: categoryId={}", dish.getCategoryId());
        String cacheKey = cacheKey(dish);
        List<DishDTO> dishDTOList = redisTemplate.opsForValue().get(cacheKey);
        if (dishDTOList != null) {
            log.info("菜品列表查询: {} 命中Redis缓存", dish.getCategoryId());
            return Result.success(dishDTOList);
        }
        dishDTOList = dishFlavorService.listWithFlavorsByCategoryId(dish.getCategoryId());
        redisTemplate.opsForValue().set(cacheKey, dishDTOList, 60, TimeUnit.MINUTES);
        return Result.success(dishDTOList);
    }

    /**
     * 添加菜品
     * @param dishDTO
     * @return
     */
    @PostMapping("/add")
    public Result<String> saveWithFlavor(@RequestBody DishDTO dishDTO) {
        log.info("添加菜品信息: {}", dishDTO);
        dishFlavorService.saveWithFlavor(dishDTO);
        String cacheKey = cacheKey(dishDTO);
        redisTemplate.delete(cacheKey);
        return Result.success("新增菜品成功");
    }

    /**
     * 修改菜品
     * @param dishDTO
     * @return
     */
    @PostMapping("/edit")
    public Result<String> updateWithFlavor(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品信息: {}", dishDTO);
        dishFlavorService.updateWithFlavor(dishDTO);
        String cacheKey = cacheKey(dishDTO);
        redisTemplate.delete(cacheKey);
        return Result.success("修改菜品成功");
    }

    /**
     * 启售/停售菜品（批量）
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<String> updateStatus(@PathVariable Integer status, @RequestParam List<Long> ids) {
        if (status == 1) {
            log.info("启售菜品id：{}", ids);
        } else {
            log.info("停售菜品id：{}", ids);
        }
        dishService.updateStatus(status, ids);
        List<Dish> dishList = dishService.listByIds(ids);
        dishList.forEach(dish -> redisTemplate.delete(cacheKey(dish)));
        return Result.success(status == 1 ? "启售成功" : "停售成功");
    }

    /**
     * 删除菜品（批量）
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> deleteByIds(@RequestParam List<Long> ids) {
        log.info("删除菜品id：{}",ids);
        dishService.removeByIdsIfStop(ids);
        return Result.success("删除成功");
    }

}
