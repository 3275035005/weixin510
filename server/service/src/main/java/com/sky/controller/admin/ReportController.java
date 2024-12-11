package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: ReportController
 * DESCRIPTION : B端-统计信息表现层接口
 */
@Slf4j
@Api(tags = "B端-统计信息表现层接口") //@Api:用户描述类的相关信息
@RestController
@RequestMapping("/admin/report") //声明访问路径为/admin/report自动映射到此方法
public class ReportController {

    //注入统计服务层接口实现类
    @Resource
    private ReportService reportService;

    /**
     * 统计指定时间端营业额数据
     *
     * @param begin 统计营业额数据的开始日期
     * @param end   统计营业额数据的结束日期
     * @return 全局通用返回Bean(统计结果数据封装Bean)
     */
    @ApiOperation("B端-统计指定时间端营业额数据")
    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> selectTurnoverStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                                             @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("统计指定时间段营业额信息,开始时间 : {} , 结束时间 : {}", begin, end);
        //调用服务层传递开始/结束时间,接收TurnoverReportVO并返回
        TurnoverReportVO turnoverReportVO = reportService.selectTurnoverStatistics(begin, end);
        return Result.success(turnoverReportVO);
    }

    /**
     * 统计指定时间段用户数据
     *
     * @param begin 统计营业额数据的开始日期
     * @param end   统计营业额数据的结束日期
     * @return 全局通用返回Bean(统计结果数据封装Bean)
     */
    @ApiOperation("B端-统计指定时间段用户数据")
    @GetMapping("/userStatistics")
    public Result<UserReportVO> selectUserStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                                     @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("统计指定时间段用户增长信息,开始时间 : {} , 结束时间 : {}", begin, end);
        //调用服务层传递开始/结束时间,接收UserReportVO并返回
        UserReportVO userReportVO = reportService.selectUserStatistics(begin, end);
        return Result.success(userReportVO);
    }

    /**
     * 统计指定时间段订单数量数据
     *
     * @param begin 统计营业额数据的开始日期
     * @param end   统计营业额数据的结束日期
     * @return 全局通用返回Bean(统计结果数据封装Bean)
     */
    @ApiOperation("B端-统计指定时间段订单数量数据")
    @GetMapping("/ordersStatistics")
    public Result<OrderReportVO> selectOrdersStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                                        @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("统计指定时间段订单数量信息,开始时间 : {} , 结束时间 : {}", begin, end);
        //调用服务层传递开始/结束时间,接收OrderReportVO并返回
        OrderReportVO orderReportVO = reportService.selectOrderStatistics(begin, end);
        return Result.success(orderReportVO);
    }

    /**
     * 统计指定时间段销售食材
     *
     * @param begin 统计营业额数据的开始日期
     * @param end   统计营业额数据的结束日期
     * @return 全局通用返回Bean(统计结果数据封装Bean)
     */
    @ApiOperation("B端-统计指定时间段销售食材/菜谱数据")
    @GetMapping("/top10")
    public Result<SalesTop10ReportVO> selectTop10(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                                  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("统计指定时间段销售食材/菜谱数据,开始时间 : {} , 结束时间 : {}", begin, end);
        //调用服务层传递开始/结束时间,接收SalesTop10ReportVO并返回
        SalesTop10ReportVO salesTop10ReportVO = reportService.selectTop10(begin, end);
        return Result.success(salesTop10ReportVO);
    }

    /**
     * 导出近30天的运营数据文件
     *
     * @param httpServletResponse 本次请求的响应对象(用于将文件数据基于输出流发送给客户端)
     */
    @ApiOperation("B端-导出近30天的运营数据文件")
    @GetMapping("/export")
    public Result export30DBusinessDate(HttpServletResponse httpServletResponse) {
        log.info("开始导出近30天的运营数据文件");
        //调用服务层传递httpServletResponse即可
        reportService.export30DBusinessDate(httpServletResponse);
        return Result.success();
    }
}
