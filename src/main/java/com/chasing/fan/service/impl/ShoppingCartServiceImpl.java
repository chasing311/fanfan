package com.chasing.fan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chasing.fan.entity.ShoppingCart;
import com.chasing.fan.mapper.ShoppingCartMapper;
import com.chasing.fan.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
    @Override
    public ShoppingCart addCart(ShoppingCart shoppingCart) {
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        if (dishId != null) {
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart shoppingCartAdded = this.getOne(queryWrapper);
        if (shoppingCartAdded != null) {
            Integer number = shoppingCartAdded.getNumber();
            shoppingCartAdded.setNumber(number + 1);
            this.updateById(shoppingCartAdded);
        } else {
            shoppingCart.setCreateTime(LocalDateTime.now());
            this.save(shoppingCart);
            shoppingCartAdded = shoppingCart;
        }
        return shoppingCartAdded;
    }

    @Override
    public void subCart(ShoppingCart shoppingCart) {
        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        //减少菜品数量
        queryWrapper.eq(dishId != null, ShoppingCart::getDishId, dishId);
        // 减少套餐数量
        queryWrapper.eq(setmealId != null, ShoppingCart::getSetmealId, setmealId);

        ShoppingCart shoppCartSubbed = this.getOne(queryWrapper);
        shoppCartSubbed.setNumber(shoppCartSubbed.getNumber() - 1);
        Integer currentNum = shoppCartSubbed.getNumber();
        if (currentNum > 0) {
            //大于0则更新
            this.updateById(shoppCartSubbed);
        }

        if (currentNum == 0) {
            //小于0则删除
            this.removeById(shoppCartSubbed.getId());
        }
    }

    @Override
    public List<ShoppingCart> listByUserId(Long userId) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        return this.list(queryWrapper);
    }

    @Override
    public void removeByUserId(Long userId) {
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(userId != null, ShoppingCart::getUserId, userId);
        this.remove(queryWrapper);
    }
}
