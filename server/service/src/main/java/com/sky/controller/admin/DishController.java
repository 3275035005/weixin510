package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: DishController
 * DESCRIPTION : B端-食材信息表现层接口
 */
@Slf4j
@Api(tags = "B端-食材信息表现层接口")
@RestController
@RequestMapping("/admin/dish")
public class DishController {

    //注入食材信息服务层接口实现Bean
    @Resource
    private DishService dishService;

    /**
     * 添加食材数据(包含食材口味)
     *
     * @param dishDTO 用于接收食材数据与食材口味数据的JavaBean
     * @return 全局通用返回结果Bean
     */
    @ApiOperation("B端-添加食材数据")
    @PostMapping
    public Result insertDish(@RequestBody DishDTO dishDTO) {
        log.info("食材数据添加的参数信息 : {}", dishDTO);
        //调用食材信息服务层传递dishDTO进行数据添加
        dishService.insertDish(dishDTO);
        return Result.success();
    }

    /**
     * 分页条件查询食材数据
     *
     * @param dishPageQueryDTO 分页查询的条件(页码/分页条数/食材名称/分类Id/食材状态)
     * @return 全局通用返回信息Bean(分页查询结果封装Bean ( 总条数 / 分页查询的结果List))
     */
    @ApiOperation("B端-分页条件查询食材数据")
    @GetMapping("/page")
    public Result<PageResult> selectDishByPage(DishPageQueryDTO dishPageQueryDTO) {
        log.info("分页条件查询食材信息的参数内容 : {}", dishPageQueryDTO);
        //调用服务层传递dishPageQueryDTO完成查询获取结果
        PageResult dishPageResult = dishService.selectDishByPage(dishPageQueryDTO);
        return Result.success(dishPageResult);
    }

    /**
     * 批量删除食材信息
     *
     * @param ids 保存了要删除的食材Id的集合
     * @return 全局通用返回信息Bean
     */
    @ApiOperation("B端-批量删除食材信息")
    @DeleteMapping
    public Result batchDeleteDish(@RequestParam List<Long> ids) {
        log.info("批量删除的食材Id集合是 : {}", ids);
        //调用服务层传递ids集合
        dishService.batchDeleteDish(ids);
        return Result.success();
    }

    /**
     * 基于食材Id查询食材信息(包含口味)
     *
     * @param dishId 食材Id
     * @return 全局通用返回信息Bean(食材完整信息[基本信息 + 口味信息])
     */
    @ApiOperation("B端-基于食材Id查询食材信息(包含口味)")
    @GetMapping("/{dishId}")
    public Result<DishVO> selectDishVOById(@PathVariable Long dishId) {
        log.info("基于食材Id查询食材信息的参数是 : {}", dishId);
        //调用服务层传递食材Id接收DishVO结果并返回
        DishVO dishVO = dishService.selectDishVOById(dishId);
        return Result.success(dishVO);
    }

    /**
     * 更新食材完整信息(包含口味)
     *
     * @param dishDTO 更新后的食材完整信息Bean(▲包含食材Id)
     * @return 全局通用返回Bean
     */
    @ApiOperation("B端-更新食材完整信息(包含口味)")
    @PutMapping
    public Result updateDish(@RequestBody DishDTO dishDTO) {
        log.info("更新后的食材完整参数信息 : {}", dishDTO);
        //调用服务层传递dishDTO完整更新
        dishService.updateDish(dishDTO);
        return Result.success();
    }

    /**
     * 食材的启售/停售功能
     *
     * @param status 修改后的食材状态
     * @param id     食材Id
     * @return 全局通用返回信息Bean
     */
    @ApiOperation("B端-食材的启售/停售功能")
    @PostMapping("/status/{status}")
    public Result modifyStatus(@PathVariable Integer status, Long id) {
        log.info("本次要修改状态的食材Id : {} , 修改的状态是 : {}", id, status);
        //将参数封装为一个Dish对象传递给服务层
        Dish dish = Dish.builder().id(id).status(status).build();
        dishService.modifyStatus(dish);
        return Result.success();
    }

    /**
     * 基于分类Id查询食材信息
     *
     * @param categoryId 分类Id
     * @return 全局通用返回Bean(分类Id下的食材集合)
     */
    @ApiOperation("B端-基于分类Id查询食材信息")
    @GetMapping("/list")
    public Result<List<Dish>> selectDishByCategoryId(Long categoryId) {
        log.info("本次基于分类Id查询食材信息的参数是 : {}", categoryId);
        //调用服务层传递分类Id查询食材信息
        List<Dish> dishList = dishService.selectDishByCategoryId(categoryId);
        return Result.success(dishList);
    }
}
