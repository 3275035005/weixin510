package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 购物车数据传输类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartDTO implements Serializable {
    //食材Id
    private Long dishId;
    //菜谱Id
    private Long setmealId;
}
