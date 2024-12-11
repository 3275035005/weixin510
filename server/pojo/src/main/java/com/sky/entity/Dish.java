package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 食材信息存储类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dish implements Serializable {
    private static final long serialVersionUID = 1L;
    //食材Id
    private Long id;
    //食材名称
    private String name;
    //食材分类id
    private Long categoryId;
    //食材价格
    private BigDecimal price;
    //食材图片
    private String image;
    //描述信息
    private String description;
    //食材状态 0:停售 1:起售
    private Integer status;
    //食材创建时间
    private LocalDateTime createTime;
    //食材更新时间
    private LocalDateTime updateTime;
    //食材创建人Id
    private Long createUser;
    //食材更新人Id
    private Long updateUser;
}
