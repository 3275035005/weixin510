package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: DishMapper

 * DESCRIPTION : B端-食材信息持久层接口
 */
@Mapper
public interface DishMapper {

    /**
     * 基于分类Id查询食材数量
     *
     * @param categoryId 食材分类Id
     * @return 分类Id对应的食材数量
     */
    Integer selectDishCountByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 添加食材数据
     *
     * @param dish 食材基本信息
     */
    @AutoFill(OperationType.INSERT)
    void insertDish(@Param("dish") Dish dish);

    /**
     * 分页条件查询食材数据
     *
     * @param dishPageQueryDTO 分页查询的条件(页码/分页条数/食材名称/分类Id/食材状态)
     * @return DishVO(包含了Dish的基本信息 + Dish对应的分类名称)的Bean ▲让MyBatis自动根据查询的结果完成数据的封装
     */
    Page<DishVO> selectDishByPage(@Param("dishPageQueryDTO") DishPageQueryDTO dishPageQueryDTO);

    /**
     * 基于食材Id查询食材信息
     *
     * @param dishId 食材Id
     * @return 食材信息
     */
    Dish selectDishById(@Param("dishId") Long dishId);

    /**
     * 批量删除食材数据
     *
     * @param ids 要删除的食材Id集合
     */
    void batchDeleteDish(@Param("ids") List<Long> ids);

    /**
     * 更新食材基本信息
     *
     * @param dish 更新后的食材信息(包含Id)
     */
    @AutoFill(OperationType.UPDATE)
    void updateDish(@Param("dish") Dish dish);

    /**
     * 基于分类Id查询食材信息
     *
     * @param categoryId 分类Id
     * @return 分类Id下的食材集合
     */
    List<Dish> selectDishByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 查询指定分类下启售食材的信息
     *
     * @param categoryId 分类Id
     * @return 分类Id下的启售食材集合
     */
    List<Dish> selectStatus1DishByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 基于食材状态查询食材数量
     *
     * @param status 食材状态
     * @return 指定状态的食材数量
     */
    Integer selectDishCountByStatus(@Param("status") Integer status);

    /**
     * 基于食材名称查询食材信息
     *
     * @param name 食材名称
     * @return 食材名称下的食材集合
     */
    List<Dish> selectDishByName(@Param("name") String name);
}
