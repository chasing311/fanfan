package com.chasing.fan.controller;

import com.chasing.fan.common.Result;
import com.chasing.fan.common.SessionUtil;
import com.chasing.fan.entity.ShoppingCart;
import com.chasing.fan.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     * @param shoppingCart
     * @param session
     * @return
     */
    @PostMapping("/add")
    public Result<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart, HttpSession session){
        shoppingCart.setUserId(SessionUtil.getUserId(session));
        log.info("购物车添加信息：{}", shoppingCart);
        ShoppingCart shoppingCartAdded = shoppingCartService.addCart(shoppingCart);
        return Result.success(shoppingCartAdded);
    }

    /**
     * 删除购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/sub")
    public Result<String> sub(@RequestBody ShoppingCart shoppingCart, HttpSession session) {
        shoppingCart.setUserId(SessionUtil.getUserId(session));
        log.info("购物车删除信息：{}", shoppingCart);
        shoppingCartService.subCart(shoppingCart);
        return Result.success("删除成功");
    }

    /**
     * 查询购物车列表
     * @param session
     * @return
     */
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(HttpSession session) {
        Long userId = SessionUtil.getUserId(session);
        log.info("查询购物车, 用户id {}", userId);
        List<ShoppingCart> shoppingCartList = shoppingCartService.listByUserId(userId);
        return Result.success(shoppingCartList);
    }

    /**
     * 清空购物车
     * @param session
     * @return
     */
    @DeleteMapping("/clean")
    public Result<String> clean(HttpSession session) {
        Long userId = SessionUtil.getUserId(session);
        log.info("清空购物车, 用户id {}", userId);
        shoppingCartService.removeByUserId(userId);
        return Result.success("成功清空购物车");
    }
}
