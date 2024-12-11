package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: OrderDetailMapper
 * DESCRIPTION : C端-订单详情持久层接口
 */
@Mapper
public interface OrderDetailMapper {
    /**
     * 批量添加订单详情
     *
     * @param orderDetailList 订单详情的List集合
     */
    void batchInsertOrderDetail(@Param("orderDetailList") List<OrderDetail> orderDetailList);

    /**
     * 基于订单Id查询订单详情
     *
     * @param orderId 订单Id
     * @return 订单详情List集合
     */
    List<OrderDetail> selectOrderDetailByOrderId(@Param("orderId") Long orderId);
}
