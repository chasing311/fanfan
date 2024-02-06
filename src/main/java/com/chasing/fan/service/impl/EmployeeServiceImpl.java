package com.chasing.fan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chasing.fan.entity.Employee;
import com.chasing.fan.mapper.EmployeeMapper;
import com.chasing.fan.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
    @Override
    public Employee getByUsername(String username) {
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, username);
        return this.getOne(queryWrapper);
    }

    @Override
    public Page<Employee> pageWithName(int page, int pageSize, String name) {
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!(name == null || name.isEmpty()), Employee::getName, name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        return this.page(pageInfo, queryWrapper);
    }
}
