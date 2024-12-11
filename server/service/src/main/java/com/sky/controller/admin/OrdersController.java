package com.sky.controller.admin;

import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrdersService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: OrdersController
 * DESCRIPTION : B端-订单信息表现层接口
 */
@Slf4j
@Api(tags = "B端-订单信息表现层接口") //@Api:用户描述类的相关信息
@RestController
@RequestMapping("/admin/order") //声明访问路径为/admin/order自动映射到此方法
public class OrdersController {

    //注入订单信息服务层接口实现类
    @Resource
    private OrdersService ordersService;

    /**
     * 商户订单搜索
     *
     * @param ordersPageQueryDTO 搜索条件参数Bean
     * @return 全局通用返回信息Bean(分页数据信息Bean ( 总条数 + OrderVO))
     */
    @ApiOperation("B端-商户订单搜索")
    @GetMapping("/conditionSearch")
    public Result<PageResult> selectOrderByCondition(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("订单搜索的条件参数是 : {}", ordersPageQueryDTO);
        //调用服务层进行查询
        PageResult orderVOPageResult = ordersService.selectOrderByCondition(ordersPageQueryDTO);
        return Result.success(orderVOPageResult);
    }

    /**
     * 统计不同状态订单数量
     *
     * @return 全局通用返回信息Bean(不同状态订单数量封装Bean)
     */
    @ApiOperation("B端-统计不同状态订单数量")
    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> statisticsOrder() {
        log.info("开始统计不同状态的订单数量");
        //调用服务层进行查询并返回
        OrderStatisticsVO orderStatisticsVO = ordersService.statisticsOrder();
        return Result.success(orderStatisticsVO);
    }

    /**
     * 商家查询订单详情
     *
     * @param orderId 订单Id
     * @return 全局通用返回Bean(订单基本信息 + 订单详细信息)
     */
    @ApiOperation("B端-商家查询订单详情")
    @GetMapping("/details/{orderId}")
    public Result<OrderVO> selectOrderVO(@PathVariable Long orderId) {
        log.info("本次查询的订单详情Id是 : {}", orderId);
        //调用服务层进行查询
        OrderVO orderVO = ordersService.selectOrder(orderId);
        return Result.success(orderVO);
    }

    /**
     * 商家接单功能
     *
     * @param orders 用于接收传递过来的要接单的订单Id
     * @return 全局通用返回信息Bean
     */
    @ApiOperation("B端-商家接单功能")
    @PutMapping("/confirm")
    public Result confirmOrder(@RequestBody Orders orders) {
        log.info("本次要接单的订单Id是 : {}", orders.getId());
        //调用服务层的方法传递orders参数
        ordersService.confirmOrder(orders);
        return Result.success();
    }

    /**
     * 商家拒绝订单
     *
     * @param orders 用于接收传递的拒绝订单原因和订单Id
     * @return 全局通用返回Bean
     */
    @ApiOperation("B端-商家拒绝订单")
    @PutMapping("/rejection")
    public Result rejectionOrder(@RequestBody Orders orders) {
        log.info("拒绝订单的Id是 : {},拒绝原因是 : {}", orders.getId(), orders.getRejectionReason());
        //调用服务层传递orders
        ordersService.rejectionOrder(orders);
        return Result.success();
    }

    /**
     * 商家取消订单
     *
     * @param orders 用于接收传递的取消订单原因和订单Id
     * @return 全局通用返回Bean
     */
    @ApiOperation("B端-商家取消订单")
    @PutMapping("/cancel")
    public Result cancelOrder(@RequestBody Orders orders) {
        log.info("取消订单的Id是 : {},取消原因是 : {}", orders.getId(), orders.getCancelReason());
        //调用服务层传递orders
        ordersService.adminCancelOrder(orders);
        return Result.success();
    }

    /**
     * 商家派送订单
     *
     * @param orderId 要派送的订单Id
     * @return 全局通用返回Bean
     */
    @ApiOperation("B端-商家派送订单")
    @PutMapping("/delivery/{orderId}")
    public Result deliveryOrder(@PathVariable Long orderId) {
        log.info("要派送的订单Id : {}", orderId);
        //调用服务层完成订单派送
        ordersService.deliveryOrder(orderId);
        return Result.success();
    }

    /**
     * 商家完成订单
     *
     * @param orderId 要完成的订单Id
     * @return 全局通用返回Bean
     */
    @ApiOperation("B端-商家完成订单")
    @PutMapping("/complete/{orderId}")
    public Result completeOrder(@PathVariable Long orderId) {
        log.info("要完成的订单Id : {}", orderId);
        //调用服务层完成订单完成
        ordersService.completeOrder(orderId);
        return Result.success();
    }
}
