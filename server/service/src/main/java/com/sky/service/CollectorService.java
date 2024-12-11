package com.sky.service;

import com.sky.entity.Collector;
import com.sky.entity.Setmeal;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: CollectorService
 * DESCRIPTION : C端-收藏信息服务层接口
 */
public interface CollectorService {

    /**
     * 基于收藏Id删除收藏
     *
     * @param collectorId 要删除的收藏Id
     */
    void deleteCollectorById(Long collectorId);


    /**
     * 基于菜谱收藏Id和用户id查询收藏
     */
    Long selectCollectorById(Long setmealId, Long employeeId);


    /**
     * 添加收藏
     */
    Long insertCollector(Collector collector);


    /**
     * 查询我的收藏
     * @return
     */
    List<SetmealVO> selectCollectorListByEmployeeId();
}
