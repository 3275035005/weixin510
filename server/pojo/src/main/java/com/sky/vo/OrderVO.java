package com.sky.vo;

import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

/**
 * 订单详情(响应数据)存储类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO extends Orders implements Serializable {
    //订单食材信息
    private String orderDishes;
    //订单详情
    private List<OrderDetail> orderDetailList;
}
