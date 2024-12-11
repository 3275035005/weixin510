package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 食材口味信息存储类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishFlavor implements Serializable {
    private static final long serialVersionUID = 1L;
    //食材口味Id
    private Long id;
    //食材口味关联食材Id
    private Long dishId;
    //口味名称
    private String name;
    //口味数据JSON数据
    private String value;
}
