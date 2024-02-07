package com.chasing.fan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chasing.fan.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
