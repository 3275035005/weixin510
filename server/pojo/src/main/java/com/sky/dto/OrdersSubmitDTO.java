package com.sky.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单提交数据传输类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrdersSubmitDTO implements Serializable {
    //地址簿Id
    private Long addressBookId;
    //付款方式
    private int payMethod;
    //备注
    private String remark;
    //打包费
    private Integer packAmount;
    //总金额
    private BigDecimal amount;
}
