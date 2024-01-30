package com.chasing.fan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chasing.fan.entity.Employee;
import com.chasing.fan.mapper.EmployeeMapper;
import com.chasing.fan.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
