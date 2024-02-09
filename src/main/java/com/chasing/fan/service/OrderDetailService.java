package com.chasing.fan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chasing.fan.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService extends IService<OrderDetail> {
    List<OrderDetail> listByOrderId(Long orderId);
}
