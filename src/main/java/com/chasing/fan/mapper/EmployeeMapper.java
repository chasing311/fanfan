package com.chasing.fan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chasing.fan.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee>{
}
