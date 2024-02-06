package com.chasing.fan.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chasing.fan.common.Result;
import com.chasing.fan.entity.Employee;
import com.chasing.fan.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2、根据页面提交的用户名username查询数据库
        Employee emp = employeeService.getByUsername(employee.getUsername());

        //3、如果没有查询到则返回登录失败结果
        if(emp == null){
            return Result.error("登录失败");
        }

        //4、密码比对，如果不一致则返回登录失败结果
        if(!emp.getPassword().equals(password)){
            return Result.error("登录失败");
        }

        //5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if(emp.getStatus() == 0){
            return Result.error("账号已禁用");
        }

        log.info("员工登录: {}", emp.getId());
        //6、登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee", emp.getId());
        return Result.success(emp);
    }

    /**
     * 员工退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request){
        log.info("员工退出: {}", request.getSession().getAttribute("employee"));
        //清理Session中保存的当前登录员工的id
        request.getSession().removeAttribute("employee");
        return Result.success("退出成功");
    }

    /**
     * 添加员工
     * @param employee
     * @return
     */
    @PostMapping("/add")
    public Result<String> save(@RequestBody Employee employee){
        log.info("新增员工信息：{}",employee.toString());
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employeeService.save(employee);
        return Result.success("添加员工成功");
    }

    /**
     * 更新员工
     * @param employee
     * @return
     */
    @PostMapping("/edit")
    public Result<String> update(@RequestBody Employee employee) {
        log.info("修改员工信息：{}",employee.toString());
        employeeService.updateById(employee);
        return Result.success("员工信息修改成功");
    }

    /**
     * 员工分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result<Page<Employee>> page(int page, int pageSize, String name) {
        log.info("员工分页查询, page={}, pageSize={}, name={}", page, pageSize, name);
        Page<Employee> pageInfo = employeeService.pageWithName(page, pageSize, name);
        return Result.success(pageInfo);
    }

    /**
     * 员工查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id) {
        log.info("员工查询, id={}", id);
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return Result.success(employee);
        }
        return Result.error("未查询到该员工信息");
    }

}
