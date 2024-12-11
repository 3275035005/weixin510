package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: DishService
 * DESCRIPTION : B端-食材信息服务层接口
 */
public interface DishService {
    /**
     * 添加食材数据(包含食材口味)
     *
     * @param dishDTO 用于接收食材数据与食材口味数据的JavaBean
     */
    void insertDish(DishDTO dishDTO);

    /**
     * 分页条件查询食材数据
     *
     * @param dishPageQueryDTO 分页查询的条件(页码/分页条数/食材名称/分类Id/食材状态)
     * @return 分页查询结果封装Bean ( 总条数 / 分页查询的结果List)
     */
    PageResult selectDishByPage(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 批量删除食材信息
     *
     * @param ids 保存了要删除的食材Id的集合
     */
    void batchDeleteDish(List<Long> ids);

    /**
     * 基于食材Id查询食材信息(包含口味)
     *
     * @param dishId 食材Id
     * @return 食材完整信息[基本信息 + 口味信息]
     */
    DishVO selectDishVOById(Long dishId);

    /**
     * 更新食材完整信息(包含口味)
     *
     * @param dishDTO 更新后的食材完整信息Bean(▲包含食材Id)
     */
    void updateDish(DishDTO dishDTO);

    /**
     * 修改食材状态(启售/停售)
     *
     * @param dish 保存了修改后状态与食材Id的Bean
     */
    void modifyStatus(Dish dish);

    /**
     * 基于分类Id查询食材信息
     *
     * @param categoryId 分类Id
     * @return 分类Id下的食材集合
     */
    List<Dish> selectDishByCategoryId(Long categoryId);

    /**
     * 查询指定分类食材和口味
     *
     * @param categoryId 分类Id
     * @return 食材基本信息 + 食材的口味信息的集合
     */
    List<DishVO> selectDishAndFlavorByCategoryId(Long categoryId);
}
