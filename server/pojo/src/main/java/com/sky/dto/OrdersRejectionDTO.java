package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 订单拒绝原因数据传输类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrdersRejectionDTO implements Serializable {
    //订单Id
    private Long id;
    //订单拒绝原因
    private String rejectionReason;
}
