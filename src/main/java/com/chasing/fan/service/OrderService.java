package com.chasing.fan.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chasing.fan.entity.Orders;
import com.chasing.fan.entity.OrdersDTO;

public interface OrderService extends IService<Orders> {
    void submit(Orders orders);
    Page<OrdersDTO> pageWithDetail(int page, int pageSize, Long userId);
}
