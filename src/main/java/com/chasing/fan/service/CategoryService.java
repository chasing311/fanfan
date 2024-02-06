package com.chasing.fan.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chasing.fan.entity.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {
    void remove(Long id);
    Page<Category> pageWithType(int page, int pageSize, String type);
    List<Category> listByType(Integer type);
}
