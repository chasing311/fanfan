package com.chasing.fan.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chasing.fan.entity.Employee;
import org.springframework.transaction.annotation.Transactional;
public interface EmployeeService extends IService<Employee> {
    Employee getByUsername(String username);
    Page<Employee> pageWithName(int page, int pageSize, String name);
}
