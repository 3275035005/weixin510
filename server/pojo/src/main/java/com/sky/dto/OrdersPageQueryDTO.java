package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 订单(条件与分页)查询数据传输类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrdersPageQueryDTO implements Serializable {
    //查询页码
    private int page;
    //每页显示条数
    private int pageSize;
    //订单编号
    private String number;
    //订单收货人手机号
    private String phone;
    //订单状态
    private Integer status;
    //订单开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginTime;
    //订单结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
    //下单用户Id
    private Long userId;
}
