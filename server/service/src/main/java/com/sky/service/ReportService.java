package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: ReportService
 * AUTHOR: SHINIAN
 * DATE: 2023/5/28
 * DESCRIPTION : B端-统计信息服务层接口
 */
public interface ReportService {

    /**
     * 统计指定时间端营业额数据
     *
     * @param begin 统计营业额数据的开始日期
     * @param end   统计营业额数据的结束日期
     * @return 统计结果数据封装Bean
     */
    TurnoverReportVO selectTurnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * 统计指定时间段用户数据
     *
     * @param begin 统计营业额数据的开始日期
     * @param end   统计营业额数据的结束日期
     * @return 统计结果数据封装Bean
     */
    UserReportVO selectUserStatistics(LocalDate begin, LocalDate end);

    /**
     * 统计指定时间段订单数量数据
     *
     * @param begin 统计营业额数据的开始日期
     * @param end   统计营业额数据的结束日期
     * @return 统计结果数据封装Bean
     */
    OrderReportVO selectOrderStatistics(LocalDate begin, LocalDate end);

    /**
     * 统计指定时间段销售食材
     *
     * @param begin 统计营业额数据的开始日期
     * @param end   统计营业额数据的结束日期
     * @return 统计结果数据封装Bean
     */
    SalesTop10ReportVO selectTop10(LocalDate begin, LocalDate end);

    /**
     * 导出近30天的运营数据文件
     *
     * @param httpServletResponse 本次请求的响应对象(用于将文件数据基于输出流发送给客户端)
     */
    void export30DBusinessDate(HttpServletResponse httpServletResponse);
}
