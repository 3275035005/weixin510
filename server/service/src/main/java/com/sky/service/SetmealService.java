package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: SetmealService

 * DESCRIPTION : B端-菜谱信息服务层接口
 */
public interface SetmealService {
    /**
     * 分页查询菜谱信息
     *
     * @param setmealPageQueryDTO 菜谱分页查询信息封装Bean
     * @return 全局通用返回信息Bean(分页结果封装Bean)
     */
    PageResult selectByPage(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 添加菜谱信息
     *
     * @param setmealDTO 菜谱信息封装Bean(包含菜谱基本信息与菜谱内食材信息)
     */
    @Transactional
    void insertSetmeal(SetmealDTO setmealDTO);

    /**
     * 删除菜谱信息
     *
     * @param ids 要删除的菜谱Id组成的List集合
     */
    @Transactional
    void batchDeleteSetmeal(List<Long> ids);

    /**
     * 基于菜谱Id查询菜谱信息
     *
     * @param setmealId 菜谱Id
     * @return 菜谱与菜谱食材关联VO
     */
    SetmealVO selectById(Long setmealId);

    /**
     * 更新菜谱信息
     *
     * @param setmealDTO 菜谱信息封装Bean(包含菜谱基本信息与菜谱内食材信息【包含SetmealId】)
     */
    @Transactional
    void updateSetmeal(SetmealDTO setmealDTO);

    /**
     * 更新菜谱状态(出售/禁售)
     *
     * @param status 更新后的状态
     * @param setmealId     菜谱Id
     */
    void modifyStatus(Long setmealId, Integer status);

    /**
     * 基于分类Id查询菜谱信息
     *
     * @param categoryId 分类Id
     * @return 指定分类菜谱List集合
     */
    List<Setmeal> selectSetmealByCategoryId(Long categoryId);

    /**
     * 查询菜谱中包含的食材信息
     *
     * @param setmealId 菜谱Id
     * @return 菜谱中食材的基本信息
     */
    List<DishItemVO> selectSetmealDishBySetmealId(Long setmealId);

    /**
     * 查询id不存在的菜谱
     * @param ids
     * @return
     */
    List<Setmeal> getListByNotId(List<Long> ids);

/**
 * 基于名称查询菜谱信息 和 食材
 */
    Map<String, Object> selectSetmealByName(String name);
}
