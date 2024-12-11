package com.sky.controller.user;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


/**
 * DESCRIPTION : C端-用户信息表现层接口
 */
@Slf4j
@Api(tags = "C端-用户信息表现层接口")
@RequestMapping("/user/employee")
@RestController("EmployeeController")
public class EmployeeController {

    //注入用户服务层实现类Bean
    @Autowired
    private EmployeeService employeeService;
    //注入Jwt令牌信息参数封装Bean(包含秘钥)
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 用户登录功能
     *
     * @param employeeLoginDTO 用户登录信息的DTO(用户名+密码)
     * @return 全局通用返回信息(用户登录响应信息VO : 包含Jwt令牌给前端用户下次访问携带)
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("B端用户登录参数 : {}", employeeLoginDTO);
        //基于服务层完成用户信息的查询
        Employee employee = employeeService.login(employeeLoginDTO);
        //根据查询出的用户信息生成Jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, employee.getId()); //将用户Id保存到令牌部分(2)中
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(), //秘钥
                jwtProperties.getUserTtl(), //过期时间
                claims); //令牌数据
        //封装用户登录响应信息VO(包含用户的基本信息+令牌数据)并返回
        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId()).userName(employee.getUsername())
                .name(employee.getName()).token(token).role(employee.getRole()).build();
        return Result.success(employeeLoginVO);
    }

    /**
     * 注册功能
     * @param employeeDTO
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody EmployeeDTO employeeDTO) {
        employeeService.insertEmployee(employeeDTO);
        return Result.success();
    }




    /**
     * 基于用户Id查询用户信息
     *
     * @return 全局通用返回信息(参数Id对应的用户完整信息)
     */
    @GetMapping("selectEmployeeById")
    public Result<Employee> selectEmployeeById() {
        //调用服务层完成用户信息的查询
        Employee employee = employeeService.selectEmployeeById(BaseContext.getCurrentId());
        return Result.success(employee);
    }

    /**
     * 更新用户基本信息
     *
     * @param employeeDTO 用于保存更新后的用户基本信息Bean(包含Id)
     * @return 全局通用返回信息
     */
    @PutMapping
    public Result updateEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employeeService.updateEmployee(employee);
        return Result.success();
    }

    /**
     * 更改用户登录密码
     *
     * @param passwordEditDTO 用于保存用户原始密码与修改后密码的Bean
     * @return 全局通用返回信息
     */
    @PutMapping("/editPassword")
    public Result editPassword(@RequestBody PasswordEditDTO passwordEditDTO) {
        log.info("本次用户密码修改前内容 : {} , 修改后内容 : {}", passwordEditDTO.getOldPassword(), passwordEditDTO.getNewPassword());
        //将当前登录的用户Id封装到passwordEditDTO中传递给服务层
        passwordEditDTO.setEmpId(BaseContext.getCurrentId());
        employeeService.editPassword(passwordEditDTO);
        return Result.success();
    }
}
