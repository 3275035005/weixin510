package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: EmployeeService

 * DESCRIPTION : B端-员工信息服务层接口
 */
public interface EmployeeService {

    /**
     * 员工登录方法
     *
     * @param employeeLoginDTO 员工登录信息的DTO(用户名+密码)
     * @return 查询到的员工信息
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 员工信息添加功能
     *
     * @param employeeDTO 用于接收员工(基本信息)的Bean
     */
    void insertEmployee(EmployeeDTO employeeDTO);

    /**
     * 条件分页查询员工信息
     *
     * @param employeePageQueryDTO 分页查询的参数(name:员工姓名 page:页码 pageSize:每页显示条数)
     * @return 保存分页查询结果Bean ( 总条数 / 分页查询的数据集合)
     */
    PageResult selectEmployeeByPage(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 更新员工信息
     *
     * @param employee 更新后的员工信息(▲其他字段并不关心是否有值 Id字段必须有值)
     */
    void updateEmployee(Employee employee);

    /**
     * 基于员工Id查询员工信息
     *
     * @param id 要查询数据的员工Id
     * @return 参数Id对应的员工完整信息
     */
    Employee selectEmployeeById(Long id);

    /**
     * 修改员工密码
     *
     * @param passwordEditDTO 用于保存员工原始密码与修改后密码的Bean
     */
    void editPassword(PasswordEditDTO passwordEditDTO);
}
