package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: OrdersMapper
 * DESCRIPTION : C端-订单信息持久层接口
 */
@Mapper
public interface OrdersMapper {
    /**
     * 添加订单基本信息
     *
     * @param orders 订单基本信息
     */
    void insertOrder(@Param("orders") Orders orders);

    /**
     * 基于订单Id与用户Id查询订单
     *
     * @param userId      用户Id
     * @param orderNumber 订单编号
     * @return 订单基本信息
     */
    Orders selectOrderByOrderNumberAndUserId(@Param("userId") Long userId, @Param("orderNumber") String orderNumber);

    /**
     * 更新订单信息
     *
     * @param orders 更新后的订单信息(包含Id)
     */
    void updateOrder(@Param("orders") Orders orders);

    /**
     * 基于条件查询订单基本信息
     *
     * @param ordersPageQueryDTO 查询条件(动态SQL判断,哪个字段有值就将哪个字段作为判断条件)
     * @return 订单基本信息Page(总条数 / 数据)
     */
    Page<Orders> selectOrderByCondition(@Param("ordersPageQueryDTO") OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 通过订单Id查询订单基本信息
     *
     * @param orderId 订单Id
     * @return 订单基本信息
     */
    Orders selectOrderById(@Param("orderId") Long orderId);

    /**
     * 统计不同状态订单数量
     *
     * @return 封装伪表数据的List集合, 里面是Map(每个Map表示一条数据) 列名:Key 数据值:Value
     */
    List<Map<String, Object>> selectOrderCount();

    /**
     * 基于订单状态和订单下单时间点查询订单信息
     *
     * @param status    订单状态
     * @param orderTime 下单时间点
     * @return 满足条件要求的订单集合
     */
    List<Orders> selectOrderByStatusAndLtOrderTime(@Param("status") Integer status, @Param("orderTime") LocalDateTime orderTime);

    /**
     * 基于订单状态与时间区间查询当天的营业额总和
     *
     * @param status           订单状态
     * @param thisDayStartTime 当天开始时间
     * @param thisDayEndTime   当天结束时间
     * @return 当天营业额总和
     */
    Double selectSumAmountByStatusAndTime(@Param("status") Integer status,
                                          @Param("startTime") LocalDateTime thisDayStartTime,
                                          @Param("endTime") LocalDateTime thisDayEndTime);

    /**
     * 基于订单状态与时间区间查询当天的订单数量
     *
     * @param status           订单状态
     * @param thisDayStartTime 当天开始时间
     * @param thisDayEndTime   当天结束时间
     * @return 当天满足查询要求的订单数量
     */
    Integer selectOrderCountByStatusAndTime(@Param("status") Integer status,
                                            @Param("startTime") LocalDateTime thisDayStartTime,
                                            @Param("endTime") LocalDateTime thisDayEndTime);

    /**
     * 基于时间区间查询区间内有效订单售出的食材/菜谱的数量的前10名(数量倒序排列)
     *
     * @param startTime 统计开始时间
     * @param endTime   统计结束时间
     * @return (销售数量 + 食材 / 菜谱名称)的JavaBean集合
     */
    List<GoodsSalesDTO> selectTop10ByTime(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
