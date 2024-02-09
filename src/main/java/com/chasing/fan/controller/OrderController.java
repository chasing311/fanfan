package com.chasing.fan.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chasing.fan.common.Result;
import com.chasing.fan.common.SessionUtil;
import com.chasing.fan.entity.Orders;
import com.chasing.fan.entity.OrdersDTO;
import com.chasing.fan.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

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
}
