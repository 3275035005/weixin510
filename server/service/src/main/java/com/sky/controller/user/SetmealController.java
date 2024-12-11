package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.CollectorService;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: SetmealController
 * DESCRIPTION : C端-菜谱信息表现层接口
 */
@Slf4j
@Api(tags = "C端-菜谱信息表现层接口")
@RequestMapping("/user/setmeal")
@RestController("userSetmealController") //防止与B端的SetmealController在容器中的名称冲突
public class SetmealController {

    //注入菜谱服务层接口实现类Bean
    @Resource
    private SetmealService setmealService;

    @Resource
    private CollectorService collectorService;

    /**
     * 基于分类Id查询菜谱信息
     *
     * @param categoryId 分类Id
     * @return 全局通用返回信息Bean(指定分类菜谱List集合)
     */
    @ApiOperation("C端-基于分类Id查询菜谱信息")
    @GetMapping("/list")
    public Result<List<Setmeal>> selectSetmealByCategoryId(Long categoryId) {
        //调用服务层进行数据查询
        List<Setmeal> setmealList = new ArrayList<>();

        if(1 == categoryId){ // 菜谱推荐，根据喜好
            List<SetmealVO> setmealVOS = collectorService.selectCollectorListByEmployeeId();
            List<Long> ids = new ArrayList<>();
            if(setmealVOS != null){
                for (SetmealVO setmealVO : setmealVOS) {
                    ids.add(setmealVO.getId());
                    Setmeal setmeal = new Setmeal();
                    BeanUtils.copyProperties(setmealVO, setmeal);
                    setmealList.add(setmeal);
                }
            }
            setmealList.addAll(setmealService.getListByNotId(ids));

        }else{
            setmealList = setmealService.selectSetmealByCategoryId(categoryId);
        }



        return Result.success(setmealList);
    }

    /**
     * 查询菜谱中包含的食材信息
     *
     * @param setmealId 菜谱Id
     * @return 全局通用返回信息Bean(菜谱中食材的基本信息), 查询是否收藏
     */
    @ApiOperation("C端-查询菜谱中包含的食材信息")
    @GetMapping("/dish/{setmealId}")
    public Result<Map<String, Object>> selectSetmealDishBySetmealId(@PathVariable Long setmealId) {

        //调用服务层传递setmealId查询结果
        List<DishItemVO> dishItemVOList = setmealService.selectSetmealDishBySetmealId(setmealId);

        Long collectorId = collectorService.selectCollectorById(setmealId, BaseContext.getCurrentId());

        Map<String, Object> map = new HashMap<>();
        map.put("list",dishItemVOList);
        map.put("collectorId",collectorId);
        return Result.success(map);
    }


    /**
     * 基于名称查询菜谱信息 和 食材
     *
     * @param name 名称
     * @return 全局通用返回信息Bean(指定分类菜谱List集合)
     */
    @ApiOperation("C端-基于名称搜索")
    @GetMapping("/search")
    public Result<Map<String, Object>> selectSetmealByName(String name) {
        return Result.success(setmealService.selectSetmealByName(name));
    }
}
