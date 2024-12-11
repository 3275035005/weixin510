package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 员工(条件与分页)查询数据传输类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeePageQueryDTO implements Serializable {
    //员工姓名
    private String name;

    // 权限
    private String role;
    //查询页码
    private int page;
    //每页显示记录数
    private int pageSize;
}
