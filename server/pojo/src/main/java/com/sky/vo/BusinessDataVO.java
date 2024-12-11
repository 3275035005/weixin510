package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 数据概览信息(响应数据)存储类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessDataVO implements Serializable {
    //营业额
    private Double turnover;
    //有效订单数
    private Integer validOrderCount;
    //订单完成率
    private Double orderCompletionRate;
    //平均客单价
    private Double unitPrice;
    //新增用户数
    private Integer newUsers;
}
