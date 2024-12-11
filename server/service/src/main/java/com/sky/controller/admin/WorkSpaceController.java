package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: WorkSpaceController
 * DESCRIPTION : B端-工作台信息表现层接口
 */
@Slf4j
@Api(tags = "B端-工作台信息表现层接口")
@RequestMapping("/admin/workspace")
@RestController
public class WorkSpaceController {

    //注入工作台服务层接口实现类
    @Resource
    private WorkSpaceService workSpaceService;

    /**
     * 查询指定时间段销售与用户数据
     *
     * @return 全局通用返回信息Bean(指定时间段销售与用户数据Bean)
     */
    @ApiOperation("B端-查询指定时间段销售与用户数据")
    @GetMapping("/businessData")
    public Result<BusinessDataVO> selectBusinessData() {
        log.info("开始查询查询指定时间段销售与用户数据");
        //调用服务层查询查询指定时间段销售与用户数据接收BusinessDataVO并响应
        LocalDateTime startTime = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.now().with(LocalTime.MAX);
        BusinessDataVO businessDataVO = workSpaceService.selectBusinessData(startTime, endTime);
        return Result.success(businessDataVO);
    }

    /**
     * 查询今日订单数量数据
     *
     * @return 全局通用返回信息Bean(今日订单数量数据Bean)
     */
    @ApiOperation("B端-查询今日订单数量数据")
    @GetMapping("/overviewOrders")
    public Result<OrderOverViewVO> selectOrdersOverview() {
        log.info("开始查询今日订单数量数据");
        //调用服务层查询今日订单数量数据接收OrderOverViewVO并响应
        OrderOverViewVO orderOverViewVO = workSpaceService.selectOrdersOverview();
        return Result.success(orderOverViewVO);
    }

    /**
     * 查询食材状态数据
     *
     * @return 全局通用返回信息Bean(食材状态数据Bean)
     */
    @ApiOperation("B端-查询食材状态数据")
    @GetMapping("/overviewDishes")
    public Result<DishOverViewVO> selectDishOverView() {
        log.info("开始查询食材状态数据");
        //调用服务层查询食材状态数据接收DishOverViewVO并响应
        DishOverViewVO dishOverViewVO = workSpaceService.selectDishOverView();
        return Result.success(dishOverViewVO);
    }

    /**
     * 查询菜谱状态数据
     *
     * @return 全局通用返回信息Bean(菜谱状态数据Bean)
     */
    @ApiOperation("B端-查询菜谱状态数据")
    @GetMapping("/overviewSetmeals")
    public Result<SetmealOverViewVO> selectSetmealOverView() {
        log.info("开始查询菜谱状态数据");
        //调用服务层查询菜谱状态数据接收SetmealOverViewVO并响应
        SetmealOverViewVO setmealOverViewVO = workSpaceService.selectSetmealOverView();
        return Result.success(setmealOverViewVO);
    }
}
