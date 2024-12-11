package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: CategoryDTO
 * USER: SHINIAN
 * DATE: 2023/5/15
 * DESCRIPTION : 分类基本数据传输类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    //分类Id
    private Long id;
    //分类名称
    private String name;
    //排序字段
    private Integer sort;
    //分类类型(1:食材分类 2:菜谱分类)
    private Integer type;
}
