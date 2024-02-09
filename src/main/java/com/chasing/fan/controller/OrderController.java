package com.chasing.fan.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chasing.fan.common.Result;
import com.chasing.fan.common.SessionUtil;
import com.chasing.fan.entity.OrderDetail;
import com.chasing.fan.entity.Orders;
import com.chasing.fan.entity.OrdersDTO;
import com.chasing.fan.entity.ShoppingCart;
import com.chasing.fan.service.OrderDetailService;
import com.chasing.fan.service.OrderService;
import com.chasing.fan.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public Result<String> submit(@RequestBody Orders orders, HttpSession session) {
        Long userId = SessionUtil.getUserId(session);
        orders.setUserId(userId);
        log.info("订单信息:{}", orders);
        orderService.submit(orders);
        return Result.success("下单成功");
    }

    /**
     * 订单分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public Result<Page<OrdersDTO>> page(int page, int pageSize, HttpSession session) {
        Long userId = SessionUtil.getUserId(session);
        log.info("订单分页查询, page={}, pageSize={}, type={}", page, pageSize, userId);
        Page<OrdersDTO> ordersDTOPage = orderService.pageWithDetail(page, pageSize, userId);
        return Result.success(ordersDTOPage);
    }

    /**
     * 再来一单
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/again")
    public Result<String> again(@RequestBody Map<String,String> map, HttpSession session) {
        Long orderId = Long.valueOf(map.get("id"));
        List<OrderDetail> orderDetailList = orderDetailService.listByOrderId(orderId);
        Long userId = SessionUtil.getUserId(session);
        List<ShoppingCart> shoppingCartList = orderDetailList.stream().map((item) -> {
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(item, shoppingCart);
            shoppingCart.setUserId(userId);
            shoppingCart.setCreateTime(LocalDateTime.now());
            return shoppingCart;
        }).collect(Collectors.toList());
        shoppingCartService.saveBatch(shoppingCartList);
        return Result.success("已添加到购物车");
    }
}
