package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.Collector;
import com.sky.entity.Setmeal;
import com.sky.mapper.CollectorMapper;
import com.sky.service.CollectorService;
import com.sky.vo.SetmealVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: CollectorServiceImpl
 * DESCRIPTION : B端-分类信息服务层接口实现类
 */
@Service
public class CollectorServiceImpl implements CollectorService {

    //注入分类持久层接口实现Bean/食材持久层接口实现Bean/菜谱持久层接口实现Bean
    @Resource
    private CollectorMapper collectorMapper;


    @Override
    public void deleteCollectorById(Long collectorId) {
        collectorMapper.deleteCollectorById(collectorId);
    }

    @Override
    public Long selectCollectorById(Long setmealId, Long employeeId) {
       return collectorMapper.selectCollectorById(setmealId, employeeId);
    }

    @Override
    public Long insertCollector(Collector collector) {
        collector.setEmployeeId(BaseContext.getCurrentId());
        collector.setCreateTime(LocalDateTime.now());
      return collectorMapper.insertCollector(collector);
    }

    @Override
    public List<SetmealVO> selectCollectorListByEmployeeId() {
        return collectorMapper.selectCollectorListByEmployeeId(BaseContext.getCurrentId());
    }
}
