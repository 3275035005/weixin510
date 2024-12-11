package com.sky.controller.user;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrdersService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: OrdersController
 * DESCRIPTION : C端-订单信息表现层接口
 */
@Slf4j
@Api(tags = "C端-订单信息表现层接口")
@RestController("userOrdersController")
@RequestMapping("/user/order")
public class OrdersController {

    //注入订单服务层接口实现类
    @Resource
    private OrdersService ordersService;

    /**
     * 用户提交订单
     *
     * @param ordersSubmitDTO 订单基本信息
     * @return 全局通用返回信息Bean(提交订单响应数据Bean ( 订单Id / 订单编号 / 订单金额 / 订单时间))
     */
    @ApiOperation("C端-用户提交订单")
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submitOrder(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("本次用户提交订单的基本信息是 : {}", ordersSubmitDTO);
        //调用服务层传递ordersSubmitDTO接收OrderSubmitVO
        OrderSubmitVO orderSubmitVO = ordersService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 用户请求支付功能
     *  更新订单状态
     *  清空购物车
     * @param ordersPaymentDTO 用于请求支付相关信息(订单号码/付款方式)
     * @return 全局通用返回信息Bean(微信支付基础信息VO)
     */
    @ApiOperation("C端-用户请求支付功能")
    @PutMapping("/payment")
    public Result<OrderPaymentVO> pay(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        ordersService.paySuccess(ordersPaymentDTO.getOrderNumber());
        log.info("本次支付订单的提交信息 : {}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = ordersService.pay(ordersPaymentDTO);
        log.info("本次支付订单的响应信息 : {}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }

    /**
     * 用户查询历史订单
     *
     * @param ordersPageQueryDTO 查询参数(页码+页数+状态)
     * @return 全局通用返回Bean(分页查询结果Bean ( 总条数 + OrderVO的List集合))
     */
    @ApiOperation("C端-用户查询历史订单")
    @GetMapping("/historyOrders")
    public Result<PageResult> selectHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("本次查询历史订单的条件是 : {}", ordersPageQueryDTO);
        //调用服务层查询
        PageResult orderVOPageResult = ordersService.selectHistoryOrders(ordersPageQueryDTO);
        return Result.success(orderVOPageResult);
    }

    /**
     * 用户查询订单详情
     *
     * @param orderId 订单Id
     * @return 全局通用返回信息Bean(订单基本信息 + 订单详情信息)
     */
    @ApiOperation("C端-用户查询订单详情")
    @GetMapping("/orderDetail/{orderId}")
    public Result<OrderVO> selectOrder(@PathVariable Long orderId) {
        log.info("本次查询订单的数据的Id是 : {}", orderId);
        //调用持久层进行查询
        OrderVO orderVO = ordersService.selectOrder(orderId);
        return Result.success(orderVO);
    }


    /**
     * 用户取消订单
     *
     * @param orderId 要取消的订单Id
     * @return 全局通用返回Bean
     */
    @ApiOperation("C端-用户取消订单")
    @PutMapping("/cancel/{orderId}")
    public Result cancelOrder(@PathVariable Long orderId) {
        log.info("本次要取消的订单Id是 : {}", orderId);
        //调用持久层完成订单信息的更新(订单状态/支付状态/取消原因/取消时间)
        ordersService.cancelOrder(orderId);
        return Result.success();
    }

    /**
     * 用户再来一单
     *
     * @param orderId 订单Id
     * @return 全局通用返回信息Bean
     */
    @ApiOperation("C端-用户再来一单")
    @PostMapping("/repetition/{orderId}")
    public Result repetitionOrder(@PathVariable Long orderId) {
        log.info("用户再来一单的订单Id : {}", orderId);
        //调用服务层传递订单Id完成购物车数据的重新添加
        ordersService.repetitionOrder(orderId);
        return Result.success();
    }

    /**
     * 用户催单
     *
     * @param orderId 用户催促接单的订单Id
     * @return 全局通用返回信息Bean
     */
    @ApiOperation("C端-用户催单")
    @GetMapping("/reminder/{orderId}")
    public Result reminderOrder(@PathVariable Long orderId) {
        log.info("本次用户催促的订单Id是 : {}", orderId);
        //调用服务层传递订单Id
        ordersService.reminderOrder(orderId);
        return Result.success();
    }
}
