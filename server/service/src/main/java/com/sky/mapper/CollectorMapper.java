package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.entity.Collector;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: CollectorMapper

 * DESCRIPTION : C端-菜谱收藏信息持久层接口
 */
@Mapper
public interface CollectorMapper {

    /**
     * 基于菜谱收藏Id删除菜谱收藏
     *
     * @param collectorId 要删除的菜谱收藏Id
     */
    void deleteCollectorById(@Param("collectorId") Long collectorId);


    /**
     * 基于菜谱收藏Id和用户id查询收藏
     */
    Long selectCollectorById(@Param("setmealId") Long setmealId,@Param("employeeId") Long employeeId);


    /**
     * 加入收藏
     * @param collector
     */
    Long insertCollector(@Param("collector") Collector collector);

    /**
     * 查询我的收藏
     * @param employeeId
     * @return
     */
    List<SetmealVO> selectCollectorListByEmployeeId(@Param("employeeId") Long employeeId);
}
