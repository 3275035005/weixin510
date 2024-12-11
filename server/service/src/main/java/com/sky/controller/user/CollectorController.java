package com.sky.controller.user;

import com.sky.entity.Collector;
import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.CollectorService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: CollectorController
 * DESCRIPTION : C端-收藏信息表现层接口
 */
@Slf4j
@Api(tags = "C端-收藏信息表现层接口")
@RequestMapping("/user/collector")
@RestController("userCollectorController") //防止与B端的CollectorController在容器中的名称冲突
public class CollectorController {
    //注入收藏信息服务层接口实现类对象
    @Resource
    private CollectorService collectorService;

    /**
     * 查询我的收藏菜谱信息
     *
     * @return 全局通用返回信息Bean(收藏List集合)
     */
    @ApiOperation("C端收藏信息")
    @GetMapping("/list")
    public Result<List<SetmealVO>> list() {
        List<SetmealVO> collectorList = collectorService.selectCollectorListByEmployeeId();
        return Result.success(collectorList);
    }

    /**
     * 加入收藏
     * @return
     */
    @PostMapping("/insertCollector")
    public Result<Long> insertCollector(@RequestBody Collector collector) {
        collectorService.insertCollector(collector);
        return Result.success(collector.getId());
    }


    /**
     * 取消收藏
     * @return
     */
    @DeleteMapping("/deleteCollectorById")
    public Result deleteCollectorById(Long collectorId) {
        collectorService.deleteCollectorById(collectorId);
        return Result.success();
    }
}
