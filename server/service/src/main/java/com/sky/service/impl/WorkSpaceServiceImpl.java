package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.EmployeeMapper;
import com.sky.mapper.OrdersMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.service.EmployeeService;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: WorkSpaceServiceImpl
 * DESCRIPTION : B端-工作台信息服务层接口实现类
 */
@Service
public class WorkSpaceServiceImpl implements WorkSpaceService {

    //注入订单持久层实现类Bean
    @Resource
    private OrdersMapper ordersMapper;
    //注入用户持久层实现类Bean
    @Resource
    private EmployeeMapper employeeMapper;
    //注入食材持久层实现类Bean
    @Resource
    private DishMapper dishMapper;
    //注入菜谱持久层实现类Bean
    @Resource
    private SetmealMapper setmealMapper;

    /**
     * 查询指定时间段销售与用户数据
     * <p>
     * BusinessDataVO包含内容:
     * - turnover : 今日销售额
     * - validOrderCount : 今日有效订单数量
     * - orderCompletionRate : 今日订单完成率
     * - unitPrice : 今日平均客单价
     * - newUsers : 今日新增长用户数
     *
     * @param startTime 开始统计时间
     * @param endTime   结束统计时间
     * @return 指定时间段销售与用户数据Bean
     */
    @Override
    public BusinessDataVO selectBusinessData(LocalDateTime startTime, LocalDateTime endTime) {
        //基于订单持久层查询今日销售额与今日有效订单数量
        Double thisDayAmount = ordersMapper.selectSumAmountByStatusAndTime(Orders.COMPLETED, startTime, endTime);
        if (Objects.isNull(thisDayAmount))
            thisDayAmount = 0.0; //如果今日没有销售额则默认为NULL手动修改为0.0
        Integer thisDayCompletedOrderCount = ordersMapper.selectOrderCountByStatusAndTime(Orders.COMPLETED, startTime, endTime);
        if (Objects.isNull(thisDayCompletedOrderCount))
            thisDayCompletedOrderCount = 0; //如果今天没有有效订单则默认为NULL手动修改为0
        Integer thisDayOrderCount = ordersMapper.selectOrderCountByStatusAndTime(null, startTime, endTime);
        //完成今日平均客单价与订单完成率的声明和计算
        Double unitPrice = 0.0;
        Double orderCompletionRate = 0.0;
        if (thisDayOrderCount != 0 && thisDayCompletedOrderCount != 0) {
            unitPrice = thisDayAmount / thisDayCompletedOrderCount;
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            unitPrice = Double.parseDouble(decimalFormat.format(unitPrice));
            orderCompletionRate = thisDayCompletedOrderCount * 1.0 / thisDayOrderCount;
        }
        //计算新增用户数
        Integer newUserCount = employeeMapper.selectCountByTime(startTime, endTime);
        return BusinessDataVO.builder()
                .newUsers(newUserCount)
                .turnover(thisDayAmount)
                .unitPrice(unitPrice)
                .validOrderCount(thisDayCompletedOrderCount)
                .orderCompletionRate(orderCompletionRate).build();
    }

    /**
     * 查询今日订单数量数据
     * <p>
     * OrderOverViewVO包括:
     * - waitingOrders : 待接单订单数量
     * - deliveredOrders : 派送中订单数量
     * - completedOrders : 已完成订单数量
     * - cancelledOrders : 已取消订单数量
     * - allOrders : 总订单数量
     *
     * @return 今日订单数量数据
     */
    @Override
    public OrderOverViewVO selectOrdersOverview() {
        //封装表示今日开始时间/结束时间的日期对象
        LocalDateTime thisDayStartTime = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime thisDayEndTime = LocalDateTime.now().with(LocalTime.MAX);
        //基于订单持久层查询今日不同状态的订单数量
        Integer waitingOrders = ordersMapper.selectOrderCountByStatusAndTime(Orders.TO_BE_CONFIRMED, thisDayStartTime, thisDayEndTime);
        Integer deliveredOrders = ordersMapper.selectOrderCountByStatusAndTime(Orders.CONFIRMED, thisDayStartTime, thisDayEndTime);
        Integer completedOrders = ordersMapper.selectOrderCountByStatusAndTime(Orders.COMPLETED, thisDayStartTime, thisDayEndTime);
        Integer cancelledOrders = ordersMapper.selectOrderCountByStatusAndTime(Orders.CANCELLED, thisDayStartTime, thisDayEndTime);
        Integer allOrders = ordersMapper.selectOrderCountByStatusAndTime(null, thisDayStartTime, thisDayEndTime);
        return OrderOverViewVO.builder()
                .waitingOrders(waitingOrders)
                .deliveredOrders(deliveredOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .allOrders(allOrders).build();
    }

    /**
     * 查询食材状态数据
     * <p>
     * DishOverViewVO包含:
     * - sold : 已启售食材数量
     * - discontinued : 已停售食材数量
     *
     * @return 全局通用返回信息Bean(食材状态数据Bean)
     */
    @Override
    public DishOverViewVO selectDishOverView() {
        //基于食材持久层查询不同状态食材数量
        Integer soldDishCount = dishMapper.selectDishCountByStatus(StatusConstant.ENABLE);
        Integer discontinuedDishCount = dishMapper.selectDishCountByStatus(StatusConstant.DISABLE);
        return DishOverViewVO.builder().sold(soldDishCount).discontinued(discontinuedDishCount).build();
    }

    /**
     * 查询菜谱状态数据
     * <p>
     * SetmealOverViewVO包含:
     * - sold : 已启售菜谱数量
     * - discontinued : 已停售菜谱数量
     *
     * @return 全局通用返回信息Bean(菜谱状态数据Bean)
     */
    @Override
    public SetmealOverViewVO selectSetmealOverView() {
        //调用菜谱持久层查询不同状态的菜谱数量
        Integer soldSetmealCount = setmealMapper.selectSetmealCOuntByStatus(StatusConstant.ENABLE);
        Integer discontinuedSetmealCount = setmealMapper.selectSetmealCOuntByStatus(StatusConstant.DISABLE);
        return SetmealOverViewVO.builder().sold(soldSetmealCount).discontinued(discontinuedSetmealCount).build();
    }
}
