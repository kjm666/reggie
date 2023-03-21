package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * description: 用户登录
     *
     * @since: 1.0.0
     * @author: KangJiaMing
     * @date: 2023/3/14 9:04
     * @Param request:
     * @Param employee:
     * @return: com.itheima.common.R<com.itheima.entity.Employee>
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
//        1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
//        2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(lqw);
//        3、如果没有查询到则返回登录失败结果
        if (null == emp) {
            return R.error("登录失败");
        }
//        4、密码比对，如果不一致则返回登录失败结果
        if (!emp.getPassword().equals(password)) {
            return R.error("密码不正确");
        }
//        5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (emp.getStatus().equals(0)) {//0代表禁用
            return R.error("账号已被禁用");
        } else {
//        6、登录成功，将员工id存入Session并返回登录成功结果
            request.getSession().setAttribute("employee", emp.getId());
            return R.success(emp);
        }
    }


    /**
     * description: 用户退出
     *
     * @since: 1.0.0
     * @author: KangJiaMing
     * @date: 2023/3/14 9:04
     * @Param request:
     * @return: com.itheima.common.R<java.lang.String>
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //1、清理session中保存的用户信息
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * description: 添加员工
     *
     * @since: 1.0.0
     * @author: KangJiaMing
     * @date: 2023/3/14 9:04
     * @Param employee:
     * @Param request:
     * @return: com.itheima.common.R<java.lang.String>
     */
    @PostMapping
    public R<String> save(@RequestBody Employee employee, HttpServletRequest request) {
        //设置密码进行md5加密处理
        String password = "123456";
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        employee.setPassword(password);
        //设置日期属性
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        Long eid = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(eid);
//        employee.setUpdateUser(eid);
        //添加员工
        boolean save = employeeService.save(employee);
        if (save) {
            return R.success("新增员工成功");
        } else {
            return R.error("新增员工失败");
        }
    }

    /**
     * description:员工信息分页查询
     *
     * @since: 1.0.0
     * @author: KangJiaMing
     * @date: 2023/3/14 19:07
     * @Param page:
     * @Param pageSize:
     * @Param name:
     * @return: com.itheima.common.R<com.baomidou.mybatisplus.extension.plugins.pagination.Page>
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        //分页构造器
        Page<Employee> employeePage = new Page<>(page, pageSize);
        //条件构造器
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        if (null != name) {
            lqw.like(Employee::getName, name);
        }
        //降序排序
        lqw.orderByDesc(Employee::getCreateTime);
        Page<Employee> page1 = employeeService.page(employeePage, lqw);
//        System.out.println(page1.getRecords());
        return R.success(page1);
    }

    /**
     * description:根据id修改员工信息
     *
     * @since: 1.0.0
     * @author: KangJiaMing
     * @date: 2023/3/14 20:07
     * @Param employee:
     * @return: com.itheima.common.R<java.lang.String>
     */
    @PutMapping()
    public R<String> update(@RequestBody Employee employee, HttpServletRequest request) {
//        log.info(employee.toString());
        //设置修改人和修改时间
        Long updateUser = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateUser(updateUser);
//        employee.setUpdateTime(LocalDateTime.now());
        //修改
        boolean b = employeeService.updateById(employee);
        if (b) {
            return R.success("员工信息修改成功");
        }
        return R.error("修改失败");
    }

    /**
     * description:
     *
     * @since: 1.0.0
     * @author: KangJiaMing
     * @date: 2023/3/14 21:39
     * @Param id: 查询员工信息
     * @return: com.itheima.reggie.common.R<com.itheima.reggie.entity.Employee>
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        if (null != employee) {
            return R.success(employee);
        }
        return R.error("查询失败");
    }

}
