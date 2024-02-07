package com.chasing.fan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chasing.fan.entity.User;

public interface UserService extends IService<User> {
    User getUserByPhone(String phone);
}
