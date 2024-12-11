package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单状态查询数据传输类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrdersConfirmDTO implements Serializable {
    //订单Id
    private Long id;
    //订单状态 1:待付款 2:待接单 3:已接单 4:派送中 5:已完成 6:已取消 7:退款
    private Integer status;

}
