package com.sky.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordEditFailedException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: EmployeeServiceImpl
 * DESCRIPTION : B端-员工信息服务层接口实现类
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    //注入员工持久层实现类Bean(MyBatis生成的代理对象)
    @Resource
    private EmployeeMapper employeeMapper;

    public static void main(String[] args) {
        System.out.println(SecureUtil.md5("123456"));
    }
    /**
     * 员工登录方法
     *
     * @param employeeLoginDTO 员工登录信息的DTO(用户名+密码)
     * @return 查询到的员工信息
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        //获取参数中的用户名与密码
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();
        //根据用户名查询数据库中的数据
        Employee employee = employeeMapper.selectEmployeeByUsername(username);
        //处理异常情况（用户名不存在）与（密码不对）
        if (employee == null) {
            //账号不存在则抛出对应异常交给全局异常处理器处理
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        if(StringUtils.isNotEmpty(employeeLoginDTO.getRole())){
            if("3".equals(employee.getRole()) ){
                throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
            }

        }

        //处理异常情况（密码错误）
        if (!SecureUtil.md5(password).equals(employee.getPassword())) {
            //密码错误则抛出对应异常交给全局异常处理器处理
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }
        //处理异常情况（用户状态为禁用）
        if (employee.getStatus().equals(StatusConstant.DISABLE)) {
            //账号被锁定则抛出对应异常交给全局异常处理器处理
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //执行到此处说明以上判断均失败员工信息合法正确则返回实体对象
        return employee;
    }

    /**
     * 员工信息添加功能
     *
     * @param employeeDTO 用于接收员工(基本信息)的Bean
     */
    @Override
    public void insertEmployee(EmployeeDTO employeeDTO) {
        //两个Bean之间数据拷贝之后是一种很常见的操作(很多框架都有数据拷贝的功能) 基于BeanUtils的copyProperties方法进行拷贝。
        //补充：BeanUtils拷贝的方式效率不是特别高！BeanCopier 效率比较高!
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        //针对于Employee对象的其他内容进行封装
        employee.setPassword(SecureUtil.md5(PasswordConstant.DEFAULT_PASSWORD)); //将默认密码MD5加密后指定为密码
        employee.setStatus(StatusConstant.ENABLE); //启用
        //调用持久层传递employee对象完成添加
        employeeMapper.insertEmployee(employee);
    }

    /**
     * 条件分页查询员工信息
     *
     * @param employeePageQueryDTO 分页查询的参数(name:员工姓名 page:页码 pageSize:每页显示条数)
     * @return 保存分页查询结果Bean ( 总条数 / 分页查询的数据集合)
     */
    @Override
    public PageResult selectEmployeeByPage(EmployeePageQueryDTO employeePageQueryDTO) {
        //分页查询(基于PageHelper分页查询插件声明分页查询条件)
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        //调用持久层将查询数据对象传递接收返回的Page(保存了分页查询完整结果的对象)
        Page<Employee> employeePage = employeeMapper.selectEmployeeByPage(employeePageQueryDTO);
        //将Page对象转换为PageResult(分页查询结果Bean)返回
        return PageResult.builder().total(employeePage.getTotal()).records(employeePage.getResult()).build();
    }

    /**
     * 更新员工信息
     *
     * @param employee 更新后的员工信息(▲其他字段并不关心是否有值 Id字段必须有值)
     */
    @Override
    public void updateEmployee(Employee employee) {
        //调用持久层完成员工信息的可选更新(员工对象中哪个字段有值就认为哪个字段更新)
        employeeMapper.updateEmployee(employee);
    }

    /**
     * 基于员工Id查询员工信息
     *
     * @param id 要查询数据的员工Id
     * @return 参数Id对应的员工完整信息
     */
    @Override
    public Employee selectEmployeeById(Long id) {
        //调用持久层完成员工信息查询并返回
        return employeeMapper.selectEmployeeById(id);
    }

    /**
     * 修改员工密码
     *
     * @param passwordEditDTO 用于保存员工原始密码与修改后密码的Bean
     */
    @Override
    public void editPassword(PasswordEditDTO passwordEditDTO) {
        //判断旧密码与当前用户的密码是否一致 [DB的员工密码是Md5加密的,passwordEditDTO是明文]
        Employee employee = employeeMapper.selectEmployeeById(passwordEditDTO.getEmpId());
        if (!employee.getPassword().equals(SecureUtil.md5(passwordEditDTO.getOldPassword())))
            throw new PasswordEditFailedException(MessageConstant.PASSWORD_EDIT_FAILED);
        //旧密码校验通过,完成员工密码的更新(封装一个Employee对象 Id + 新密码),调用持久层完成更新
        Employee newEmployee = Employee.builder().id(passwordEditDTO.getEmpId()).password(SecureUtil.md5(passwordEditDTO.getNewPassword())).build();
        employeeMapper.updateEmployee(newEmployee);
    }
}
