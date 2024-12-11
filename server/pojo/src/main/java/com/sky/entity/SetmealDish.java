package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 菜谱食材关系信息存储类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetmealDish implements Serializable {

    private static final long serialVersionUID = 1L;

    //菜谱食材关联关系Id
    private Long id;
    //菜谱Id
    private Long setmealId;
    //菜谱关联食材Id
    private Long dishId;
    //食材名称(冗余字段)
    private String name;
    //食材原价
    private BigDecimal price;
    //食材份数
    private Integer copies;
}
