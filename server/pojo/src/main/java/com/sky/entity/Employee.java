package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户信息存储类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    //用户Id
    private Long id;
    //用户登录名称
    private String username;
    //用户真实姓名
    private String name;
    //用户登录密码
    private String password;
    //用户手机号码
    private String phone;
    //用户性别
    private String sex;
    //用户身份证号
    private String idNumber;
    //用户状态(0:禁用 1:启用)
    private Integer status;
    //用户信息创建时间
    private LocalDateTime createTime;
    //用户信息更新时间
    private LocalDateTime updateTime;
    //用户信息创建人Id
    private Long createUser;
    //用户信息更新人Id
    private Long updateUser;
    // 1管理员 2商家 3用户
    private String role;

    // 口味辣度 0不辣 1微辣 2中辣 3重辣
    private String pungencyType;

    // 口味甜度 0无糖 1少糖 2半糖 3多糖 4全糖
    private String sweetType;

    // 地区
    private String address;
}
