package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.EmployeeMapper;
import com.sky.mapper.OrdersMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkSpaceService;
import com.sky.vo.*;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: ReportServiceImpl
 * AUTHOR: SHINIAN
 * DATE: 2023/5/28
 * DESCRIPTION : B端-统计信息服务层接口实现类
 */
@Service
public class ReportServiceImpl implements ReportService {

    //注入订单信息持久层接口实现类
    @Resource
    private OrdersMapper ordersMapper;
    //注入用户信息持久层接口实现类
    @Resource
    private EmployeeMapper employeeMapper;
    //注入工作台服务层接口实现类(不注入Mapper的原因,工作服务层的方法可以直接返回BusinessDataVO)
    @Resource
    private WorkSpaceService workSpaceService;

    /**
     * 统计指定时间端营业额数据
     *
     * @param begin 统计营业额数据的开始日期
     * @param end   统计营业额数据的结束日期
     * @return 统计结果数据封装Bean
     */
    @Override
    public TurnoverReportVO selectTurnoverStatistics(LocalDate begin, LocalDate end) {
        //基于累加法将要查询的时间段内的每一天的LocalDate对象保存到ArrayList集合中
        ArrayList<LocalDate> dateArrayList = new ArrayList<>();
        //创建用于保存每一天营业额的ArrayList集合
        ArrayList<Double> everyDayTurnoverList = new ArrayList<>();
        dateArrayList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateArrayList.add(begin);
        }
        //遍历dateArrayList集合,获取到要查询营业额的每一天的LocalDate对象
        dateArrayList.forEach(date -> {
            //例:SELECT SUM(amount) FROM orders WHERE status = 5 AND order_time > '2023-05-25 00:00:00' AND order_time < '2023-05-25 23:59:59'
            //实际要查询的时候,不能仅仅基于年月日,还要有时分秒,将date转换为表示当天开始时间/结束时间LocalDateTime对象(2个)
            LocalDateTime thisDayStartTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime thisDayEndTime = LocalDateTime.of(date, LocalTime.MAX);
            //基于订单基本信息的持久层查询当天的营业额信息(要统计的订单状态/当天开始时间/当天结束时间)
            Double thisDayTurnover = ordersMapper.selectSumAmountByStatusAndTime(Orders.COMPLETED, thisDayStartTime, thisDayEndTime);
            if (Objects.isNull(thisDayTurnover))
                thisDayTurnover = 0.0;  //如果当天没有营业额为了避免出现空指针异常,手动赋值为0.0
            everyDayTurnoverList.add(thisDayTurnover);
        });
        //基于StringUtils工具类有一个方法join方法,可以传递一个集合,并且传递分隔符,自动将集合中的内容按照 元素分隔符元素分隔符 拼接为一个字符串
        String dateResultString = StringUtils.join(dateArrayList, ",");
        String turnoverResultString = StringUtils.join(everyDayTurnoverList, ",");
        return TurnoverReportVO.builder().dateList(dateResultString).turnoverList(turnoverResultString).build();
    }

    /**
     * 统计指定时间段用户数据
     *
     * @param begin 统计营业额数据的开始日期
     * @param end   统计营业额数据的结束日期
     * @return 统计结果数据封装Bean
     */
    @Override
    public UserReportVO selectUserStatistics(LocalDate begin, LocalDate end) {
        //基于累加法将要查询的时间段内的每一天的LocalDate对象保存到ArrayList集合中
        ArrayList<LocalDate> dateArrayList = new ArrayList<>();
        //创建两个集合分别保存每一天的新增用户数量/总用户数
        ArrayList<Integer> newUserCountList = new ArrayList<>();
        ArrayList<Integer> totalUserCountList = new ArrayList<>();
        dateArrayList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateArrayList.add(begin);
        }
        //遍历dateArrayList集合,获取到要查询用户数量的每一天的LocalDate对象
        dateArrayList.forEach(date -> {
            //SELECT COUNT(id) FROM user WHERE create_time >= '2023-05-20 00:00:00' AND create_time <= '2023-05-20 23:59:59';
            //SELECT COUNT(id) FROM user WHERE create_time <= '2023-05-20 23:59:59';
            LocalDateTime thisDayStartTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime thisDayEndTime = LocalDateTime.of(date, LocalTime.MAX);
            //查询满足时间区间要求的新增用户数
            Integer newUserCount = employeeMapper.selectCountByTime(thisDayStartTime, thisDayEndTime);
            if (Objects.isNull(newUserCount))
                newUserCount = 0;
            newUserCountList.add(newUserCount);
            //查询满足时间区间要求的总用户数
            Integer totalUserCount = employeeMapper.selectCountByTime(null, thisDayEndTime);
            if (Objects.isNull(totalUserCount))
                totalUserCount = 0;
            totalUserCountList.add(totalUserCount);
        });
        //将三个集合转换为满足UserReportVO成员变量数据格式的字符串
        String dateResultString = StringUtils.join(dateArrayList, ","); //2023-05-23,2023-05-24
        String newUserCountListString = StringUtils.join(newUserCountList, ","); //4,5
        String totalUserCountListString = StringUtils.join(totalUserCountList, ","); //10,14
        return UserReportVO.builder()
                .dateList(dateResultString)
                .newUserList(newUserCountListString)
                .totalUserList(totalUserCountListString).build();
    }

    /**
     * 统计指定时间段订单数量数据
     * OrderReportVO的组成部分有6个:
     * - dateList : 查询时间段内的每一个日期的字符串 "2023-05-23,2023-05-24,2023-05-25"
     * - orderCountList : 查询时间端内的每一天的总订单数的字符串 "10,5,5"
     * - validOrderCountList : 查询时间段内每一天的有效订单数的字符串 "10,4,4"
     * - totalOrderCount : 查询时间段内的总订单数
     * - validOrderCount : 查询时间端内的有效订单数
     * - orderCompletionRate : 查询时间段内的订单完成率 = 查询时间段内的有效订单数 / 查询时间端内的总订单数
     *
     * @param begin 统计营业额数据的开始日期
     * @param end   统计营业额数据的结束日期
     * @return 统计结果数据封装Bean
     */
    @Override
    public OrderReportVO selectOrderStatistics(LocalDate begin, LocalDate end) {
        //基于累加法将要查询的时间段内的每一天的LocalDate对象保存到ArrayList集合中
        ArrayList<LocalDate> dateArrayList = new ArrayList<>();
        //创建两个ArrayList集合分别用于保存每一天的有效订单数/总订单数
        ArrayList<Integer> everydayCompletedOrderCountList = new ArrayList<>();
        ArrayList<Integer> everydayOrderCountList = new ArrayList<>();
        dateArrayList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateArrayList.add(begin);
        }
        dateArrayList.forEach(date -> {
            LocalDateTime thisDayStartTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime thisDayEndTime = LocalDateTime.of(date, LocalTime.MAX);
            //使用ordersMapper查询下单时间在thisDayStartTime和thisDayEndTime之间,并且订单状态是已完成的订单数量
            //SELECT COUNT(id) FROM orders WHERE status = 5 AND order_time >= '2023-05-23 00:00:00' AND order_time <= '2023-05-23 23:59:59';
            Integer thisDayCompletedOrderCount = ordersMapper.selectOrderCountByStatusAndTime(Orders.COMPLETED, thisDayStartTime, thisDayEndTime);
            if (Objects.isNull(thisDayCompletedOrderCount))
                thisDayCompletedOrderCount = 0; //如果当天没有已完成订单,默认是NULL,手动赋值为0
            everydayCompletedOrderCountList.add(thisDayCompletedOrderCount);
            //使用ordersMapper查询下单时间在thisDayStartTime和thisDayEndTime之间的订单数量 (订单的状态任意,所以查询的时候状态参数传递了null)
            //SELECT COUNT(id) FROM orders WHERE order_time >= '2023-05-23 00:00:00' AND order_time <= '2023-05-23 23:59:59';
            Integer thisDayOrderCount = ordersMapper.selectOrderCountByStatusAndTime(null, thisDayStartTime, thisDayEndTime);
            if (Objects.isNull(thisDayOrderCount))
                thisDayOrderCount = 0; //如果当天没有任何订单,默认是NULL,手动赋值为0
            everydayOrderCountList.add(thisDayOrderCount);
        });
        //Stream流:引用流/基本流(流中的元素都是基本类型的元素)【提供了一些好用的方法,对流中的元素求和/求平均值/求最大值/求最小值】
        //计算总订单数 : 先将Integer的流转换为IntStream(基本流)再基于基本流提供的sum方法将流中的元素累加后返回
        int totalOrderCount = everydayOrderCountList.stream().mapToInt(Integer::intValue).sum();
        //计算有效订单数
        int validOrderCount = everydayCompletedOrderCountList.stream().mapToInt(Integer::intValue).sum();
        //计算订单完成率之前(判断总订单数是否为0,如果为0,订单完成率就是0.0,如果不是0,再进行计算)
        Double orderCompletionRate = 0.0;
        if (totalOrderCount != 0)
            orderCompletionRate = validOrderCount * 1.0 / totalOrderCount;
        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateArrayList, ","))
                .orderCountList(StringUtils.join(everydayOrderCountList, ","))
                .validOrderCountList(StringUtils.join(everydayCompletedOrderCountList, ","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate).build();
    }

    /**
     * 统计指定时间段销售食材
     *
     * @param begin 统计营业额数据的开始日期
     * @param end   统计营业额数据的结束日期
     * @return 统计结果数据封装Bean
     */
    @Override
    public SalesTop10ReportVO selectTop10(LocalDate begin, LocalDate end) {
        //和之前不同,这里不需要将begin和end中的每一天LocalDate对象保存到集合中了(前台要求查询的就是该时间段的总体数据,并不要求查询每一天)
        //但是为了满足SQL语句中的精准查询,还需要将begin和end转换为当天的最开始时间/当天的结束时间
        LocalDateTime startTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        //SQL语句编写好之后,得到的伪表的结果是列名:number(售出数量),name(食材/菜谱名称),将每一行数据封装为GoodSalesDTO集合,调用持久层进行查询
        List<GoodsSalesDTO> goodsSalesDTOList = ordersMapper.selectTop10ByTime(startTime, endTime);
        //从goodsSalesDTOList中获取所有食材/菜谱名称组成的List集合
        List<String> nameList = goodsSalesDTOList.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        //从goodsSalesDTOList中获取食材/菜谱销售份数组成的List集合
        List<Integer> numberList = goodsSalesDTOList.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());
        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList, ","))
                .numberList(StringUtils.join(numberList, ","))
                .build();
    }

    /**
     * 导出近30天的运营数据文件
     *
     * @param httpServletResponse 本次请求的响应对象(用于将文件数据基于输出流发送给客户端)
     */
    @SneakyThrows
    @Override
    public void export30DBusinessDate(HttpServletResponse httpServletResponse) {
        //(1)获取要读取的运营数据报表.xlsx文件
        //is:绑定了当前项目resources下template文件夹中运营数据报表.xlsx的字节输入流
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("template\\运营数据报表.xlsx");

        //(2)将is绑定的文件内容读取到一个XSSFWorkBook
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        XSSFSheet sheet1 = xssfWorkbook.getSheetAt(0);

        //(3)完成部分1的内容声明 声明当前数据报表的数据从哪一天到哪一天(近30天) 获取表示30天的日期对象和昨天的日期对象形成一个日期区间
        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = LocalDate.now().minusDays(1);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        String startDateString = dateTimeFormatter.format(startDate); //2023年4月29号
        String endDateString = dateTimeFormatter.format(endDate); //2023年5月28日
        sheet1.getRow(1).getCell(1).setCellValue(startDateString + "至" + endDateString);

        //(4)完成部分2的内容声明 计算从startDate 00:00:00开始到endDate 23:59:59秒之间的运营数据
        BusinessDataVO businessDataVO = workSpaceService.selectBusinessData(LocalDateTime.of(startDate, LocalTime.MIN), LocalDateTime.of(endDate, LocalTime.MAX));
        sheet1.getRow(3).getCell(2).setCellValue(businessDataVO.getTurnover());
        sheet1.getRow(3).getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
        sheet1.getRow(3).getCell(6).setCellValue(businessDataVO.getNewUsers());
        sheet1.getRow(4).getCell(2).setCellValue(businessDataVO.getValidOrderCount());
        sheet1.getRow(4).getCell(4).setCellValue(businessDataVO.getUnitPrice());

        //(5)完成部分3的内容声明 计算从startDate开始的每一天的营业数据
        for (int rowIndex = 7; rowIndex < 37; rowIndex++, startDate = startDate.plusDays(1)) {
            //基于rowIndex获取本次要填充数据的行索引,每循环一次说明填充了一行,++之后就可以获取下一行
            XSSFRow thisRow = sheet1.getRow(rowIndex);
            BusinessDataVO thisDayBusinessData = workSpaceService.selectBusinessData(LocalDateTime.of(startDate, LocalTime.MIN), LocalDateTime.of(startDate, LocalTime.MAX));
            thisRow.getCell(1).setCellValue(dateTimeFormatter.format(startDate)); //日期
            thisRow.getCell(2).setCellValue(thisDayBusinessData.getTurnover()); //当天营业额
            thisRow.getCell(3).setCellValue(thisDayBusinessData.getValidOrderCount()); //有效订单数
            thisRow.getCell(4).setCellValue(thisDayBusinessData.getOrderCompletionRate()); //订单完成率
            thisRow.getCell(5).setCellValue(thisDayBusinessData.getUnitPrice()); //平均客单价
            thisRow.getCell(6).setCellValue(thisDayBusinessData.getNewUsers()); //新增用户数
        }

        //(6)获取响应对象的输出流 并且将Excel文件对象写给客户端
        ServletOutputStream os = httpServletResponse.getOutputStream();
        xssfWorkbook.write(os);

        //(7)释放资源
        is.close();
        os.close();
        xssfWorkbook.close();
    }
}
