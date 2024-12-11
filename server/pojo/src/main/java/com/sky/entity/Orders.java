package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单信息存储类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Orders implements Serializable {
    private static final long serialVersionUID = 1L;
    //订单Id
    private Long id;
    //订单编号(使用时间戳)
    private String number;
    //订单状态 1:待付款 2:待接单 3:已接单 4:派送中 5:已完成 6:已取消 7:退款
    private Integer status;
    //下单用户Id
    private Long userId;
    //地址Id
    private Long addressBookId;
    //下单时间
    private LocalDateTime orderTime;
    //结账时间
    private LocalDateTime checkoutTime;
    //支付方式 1:微信 2:支付宝
    private Integer payMethod;
    //支付状态 0:未支付 1:已支付 2:退款
    private Integer payStatus;
    //订单实收金额
    private BigDecimal amount;
    //订单备注信息
    private String remark;
    //下单用户用户名
    private String userName;
    //下单用户手机号
    private String phone;
    //下单用户地址
    private String address;
    //下单用户收货人姓名
    private String consignee;
    //订单取消原因
    private String cancelReason;
    //订单拒绝原因
    private String rejectionReason;
    //订单取消时间
    private LocalDateTime cancelTime;
    //送达时间
    private LocalDateTime deliveryTime;
    //订单打包费
    private Integer packAmount;
    //反馈意见
    public String feedback;

    /*-----------------订单状态常量声明(开始)-----------------*/
    public static final Integer PENDING_PAYMENT = 1;
    public static final Integer TO_BE_CONFIRMED = 2;
    public static final Integer CONFIRMED = 3;
    public static final Integer DELIVERY_IN_PROGRESS = 4;
    public static final Integer COMPLETED = 5;
    public static final Integer CANCELLED = 6;
    /*-----------------订单状态常量声明(结束)-----------------*/

    /*-----------------订单支付常量声明(开始)-----------------*/
    public static final Integer UN_PAID = 0;
    public static final Integer PAID = 1;
    public static final Integer REFUND = 2;
    /*-----------------订单支付常量声明(结束)-----------------*/


}
