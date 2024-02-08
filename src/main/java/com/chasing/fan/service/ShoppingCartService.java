package com.chasing.fan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chasing.fan.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {
    ShoppingCart addCart(ShoppingCart shoppingCart);
    void subCart(ShoppingCart shoppingCart);
    List<ShoppingCart> listByUserId(Long userId);
    void removeByUserId(Long userId);
}
