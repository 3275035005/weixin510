package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 分类信息存储类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;
    //分类Id
    private Long id;
    //类型 1:食材分类 2:菜谱分类
    private Integer type;
    //分类名称
    private String name;
    //顺序
    private Integer sort;
    //分类状态 0:禁用 1:启用
    private Integer status;
    //分类创建时间
    private LocalDateTime createTime;
    //分类更新时间
    private LocalDateTime updateTime;
    //分类创建人Id
    private Long createUser;
    //分类更新人Id
    private Long updateUser;
}
