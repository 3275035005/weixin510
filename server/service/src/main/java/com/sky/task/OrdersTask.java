package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrdersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: OrdersTask
 * AUTHOR: SHINIAN
 * DATE: 2023/5/27
 * DESCRIPTION : 订单状态的定时任务(处理未付款的订单/派送中的订单)
 */
@Slf4j
@Component //注入到IOC容器中(只有类在容器中存在,SpringTas相k才可以继续扫描类中的关方法)
public class OrdersTask {

    //注入订单持久层接口实现类(用于查询订单信息)
    @Resource
    private OrdersMapper ordersMapper;

    /**
     * 定时处理超过下单超过15分钟未付款的订单信息
     * SELECT * FROM orders WHERE status = 1 AND order_time < 当前时间的15分钟前
     */
    @Scheduled(cron = "0 * * * * ?") //每月每天每日每时每分钟的0秒执行
    public void scheduledProcessPendingPaymentOrders() {
        log.info("【开始】 处理下单超过15分钟未付款的订单信息");
        //查询下单时间小于当前时间的15分钟前并且状态是未付款的订单信息
        LocalDateTime orderTime = LocalDateTime.now().minusMinutes(15); //封装查询时间节点
        List<Orders> ordersList = ordersMapper.selectOrderByStatusAndLtOrderTime(Orders.PENDING_PAYMENT, orderTime);
        //遍历集合将集合中的每一个订单信息中的状态修改为取消/声明取消原因和取消时间,再重新更新到表中
        ordersList.forEach(orders -> {
            orders.setStatus(Orders.CANCELLED); //指定订单状态为已取消
            orders.setCancelTime(LocalDateTime.now()); //指定取消时间为当前时间
            orders.setCancelReason("订单支付超时自动取消");
            ordersMapper.updateOrder(orders);
        });
    }

    /**
     * 定时处理昨天一直处于派送中的订单信息(修改为已完成)
     * SELECT * FROM orders WHERE status = 4 AND order_time < 当前时间的1小时前
     */
    @Scheduled(cron = "0 0 1 * * ?") //每月每天每日凌晨1点0分0秒执行
    //@Scheduled(cron = "0 45 10 27 5 ? ") //临:5月27号10:46分0秒执行
    public void scheduledProcessDeliveryInProgressOrders() {
        log.info("【开始】 处理昨日派送中但未完成的订单信息");
        //查询下单时间小于当前时间的一小时前并且订单状态是配送中的订单信息
        LocalDateTime orderTime = LocalDateTime.now().minusHours(1);
        List<Orders> ordersList = ordersMapper.selectOrderByStatusAndLtOrderTime(Orders.DELIVERY_IN_PROGRESS, orderTime);
        ordersList.forEach(orders -> {
            orders.setStatus(Orders.COMPLETED); //声明状态为已完成
            orders.setDeliveryTime(LocalDateTime.now()); //声明实际送达时间为当前时间(因为正常下手动点击完成的时候)
            ordersMapper.updateOrder(orders);
        });
    }
}
