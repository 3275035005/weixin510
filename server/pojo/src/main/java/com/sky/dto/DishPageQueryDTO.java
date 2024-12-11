package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 食材(条件与分页)查询数据传输类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DishPageQueryDTO implements Serializable {
    //查询页码
    private int page;
    //每页显示条数
    private int pageSize;
    //食材名称
    private String name;
    //分类id
    private Integer categoryId;
    //状态 0:禁用 1:启用
    private Integer status;
}
