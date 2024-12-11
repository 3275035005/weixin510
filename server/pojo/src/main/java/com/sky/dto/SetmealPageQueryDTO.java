package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 菜谱(分页与条件)查询数据传输类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SetmealPageQueryDTO implements Serializable {
    //查询页码
    private int page;
    //每页显示条数
    private int pageSize;
    //菜谱名称
    private String name;
    //分类Id
    private Integer categoryId;
    //状态 0:表示禁用 1:表示启用
    private Integer status;

}
