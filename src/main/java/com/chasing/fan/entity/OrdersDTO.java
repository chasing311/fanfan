package com.chasing.fan.entity;

import lombok.Data;

import java.util.List;

@Data
public class OrdersDTO extends Orders {
    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
}
