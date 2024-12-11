package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 分类(条件与分页)查询数据传输类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryPageQueryDTO implements Serializable {
    //查询页码
    private int page;
    //每页记录数
    private int pageSize;
    //分类名称
    private String name;
    //分类类型 1:食材分类  2:菜谱分类
    private Integer type;

}
