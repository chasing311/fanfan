package com.chasing.fan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chasing.fan.entity.Category;

public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
