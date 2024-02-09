package com.chasing.fan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chasing.fan.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
