package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 员工添加数据传输类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO implements Serializable {
    //员工Id
    private Long id;
    //登录用户名
    private String username;
    //真实姓名
    private String name;
    //手机号码
    private String phone;
    //员工性别
    private String sex;
    //身份证号
    private String idNumber;

    private String role;

    // 口味辣度 0不辣 1微辣 2中辣 3重辣
    private String pungencyType;

    // 口味甜度 0无糖 1少糖 2半糖 3多糖 4全糖
    private String sweetType;

}
