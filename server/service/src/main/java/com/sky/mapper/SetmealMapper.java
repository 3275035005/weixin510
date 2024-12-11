package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: SetmealMapper

 * DESCRIPTION : B端-菜谱信息持久层接口
 */
@Mapper
public interface SetmealMapper {

    /**
     * 基于分类Id聚合查询菜谱数量
     *
     * @param categoryId 分类Id
     * @return 分类Id对应的菜谱数量
     */
    Integer selectSetmealCountByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 基于食材Id查询菜谱数量
     *
     * @param dishId 食材对应Id
     * @return 食材Id对应的菜谱数量
     */
    Integer selectSetmealCountByDishId(@Param("dishId") Long dishId);

    /**
     * 分页查询菜谱信息
     *
     * @param setmealPageQueryDTO 菜谱分页查询信息封装Bean
     * @return 分页查询结果封装Bean
     */
    Page<SetmealVO> selectByPage(@Param("setmealPageQueryDTO") SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 添加菜谱基本信息
     *
     * @param setmeal 菜谱基本信息
     */
    @AutoFill(OperationType.INSERT)
    void insertSetmeal(@Param("setmeal") Setmeal setmeal);

    /**
     * 批量添加菜谱中的食材信息
     *
     * @param setmealDishList 菜谱食材信息List集合
     */
    void batchInsertSetmealDish(@Param("setmealDishList") List<SetmealDish> setmealDishList);

    /**
     * 基于菜谱Id查询菜谱状态
     *
     * @param setmealId 菜谱Id
     * @return 菜谱状态
     */
    Integer selectStatusById(@Param("setmealId") Long setmealId);

    /**
     * 基于菜谱Id删除菜谱基本信息
     *
     * @param deleteSetmealId 菜谱Id
     */
    void deleteById(@Param("deleteSetmealId") Long deleteSetmealId);

    /**
     * 基于菜谱Id删除菜谱信息表中的关联信息
     *
     * @param deleteSetmealId 菜谱Id
     */
    void deleteSetmealDishBySetmealId(@Param("deleteSetmealId") Long deleteSetmealId);

    /**
     * 基于菜谱Id查询菜谱基本信息
     *
     * @param setmealId 菜谱Id
     * @return 菜谱基本信息
     */
    Setmeal selectById(@Param("setmealId") Long setmealId);

    /**
     * 基于菜谱Id查询菜谱食材表关联信息
     *
     * @param setmealId 菜谱Id
     * @return 菜谱食材信息List集合
     */
    List<SetmealDish> selectSetmealDishBySetmealId(@Param("setmealId") Long setmealId);

    /**
     * 基于菜谱Id更新菜谱信息
     *
     * @param newSetmeal 更新后的菜谱信息(包含Id)
     */
    @AutoFill(OperationType.UPDATE)
    void updateSetmeal(@Param("setmeal") Setmeal newSetmeal);

    /**
     * 根据分类Id查询菜谱列表信息功能
     *
     * @param conditionSetmeal 菜谱查询信息
     * @return 菜谱列表
     */
    List<Setmeal> selectByCondition(@Param("conditionSetmeal") Setmeal conditionSetmeal);

    /**
     * 基于菜谱状态查询菜谱数量
     *
     * @param status 菜谱状态
     * @return 指定状态的菜谱数量
     */
    Integer selectSetmealCOuntByStatus(@Param("status") Integer status);

    List<Setmeal> getListByNotId(@Param("ids") List<Long> ids);
}
