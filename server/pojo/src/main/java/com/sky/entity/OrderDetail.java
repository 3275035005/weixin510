package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单明细信息存储类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    //订单详情Id
    private Long id;
    //订单详情食材/菜谱名称
    private String name;
    //订单详情关联订单Id
    private Long orderId;
    //食材Id
    private Long dishId;
    //菜谱Id
    private Long setmealId;
    //订单详情食材口味Id
    private String dishFlavor;
    //订单详情食材/菜谱数量
    private Integer number;
    //订单详情金额
    private BigDecimal amount;
    //订单详情图片
    private String image;
}
