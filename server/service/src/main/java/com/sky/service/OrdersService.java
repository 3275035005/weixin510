package com.sky.service;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: OrdersService
 * DESCRIPTION : C端-订单信息服务层接口
 */
public interface OrdersService {

    /**
     * 用户提交订单
     *
     * @param ordersSubmitDTO 订单基本信息
     * @return 提交订单响应数据Bean ( 订单Id / 订单编号 / 订单金额 / 订单时间)
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 用户请求支付功能
     *
     * @param ordersPaymentDTO 用于请求支付相关信息(订单号码/付款方式)
     * @return 微信支付基础信息VO
     */
    OrderPaymentVO pay(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功功能(更改订单状态)
     *
     * @param outTradeNo 订单编号
     */
    void paySuccess(String outTradeNo);

    /**
     * 用户查询历史订单
     *
     * @param ordersPageQueryDTO 查询参数(页码+页数+状态)
     * @return 分页查询结果Bean ( 总条数 + OrderVO的List集合)
     */
    PageResult selectHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 用户查询订单详情
     *
     * @param orderId 订单Id
     * @return 订单基本信息 + 订单详情信息
     */
    OrderVO selectOrder(Long orderId);

    /**
     * 用户取消订单
     *
     * @param orderId 要取消的订单Id
     */
    void cancelOrder(Long orderId);

    /**
     * 用户再来一单
     *
     * @param orderId 订单Id
     */
    void repetitionOrder(Long orderId);

    /**
     * 商户订单搜索
     *
     * @param ordersPageQueryDTO 搜索条件参数Bean
     * @return 分页数据信息Bean ( 总条数 + OrderVO)
     */
    PageResult selectOrderByCondition(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 统计不同状态订单数量
     *
     * @return 不同状态订单数量封装Bean
     */
    OrderStatisticsVO statisticsOrder();

    /**
     * 商家接单功能
     *
     * @param orders 用于接收传递过来的要接单的订单Id
     */
    void confirmOrder(Orders orders);

    /**
     * 商家拒绝订单
     *
     * @param orders 用于接收传递的拒绝订单原因和订单Id
     */
    void rejectionOrder(Orders orders);

    /**
     * 商家取消订单
     *
     * @param orders 用于接收传递的取消订单原因和订单Id
     */
    void adminCancelOrder(Orders orders);

    /**
     * 商家派送订单
     *
     * @param orderId 要派送的订单Id
     */
    void deliveryOrder(Long orderId);

    /**
     * 商家完成订单
     *
     * @param orderId 要完成的订单Id
     */
    void completeOrder(Long orderId);

    /**
     * 用户催单
     *
     * @param orderId 用户催促接单的订单Id
     */
    void reminderOrder(Long orderId);
}
