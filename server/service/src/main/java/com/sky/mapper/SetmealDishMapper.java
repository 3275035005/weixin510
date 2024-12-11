package com.sky.mapper;

import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.vo.DishItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: SetmealDishMapper
 * USER: SHINIAN
 * DATE: 2023/5/17
 * DESCRIPTION : B端-菜谱食材关联关系持久层
 */
@Mapper
public interface SetmealDishMapper {

    /**
     * 查询食材菜谱信息关联表中与ids集合中的id有关联关系的数据条数
     *
     * @param ids 要判断的食材Id集合
     * @return 有关联关系的数据条数
     */
    public Long selectCountByDishIds(@Param("ids") List<Long> ids);

    /**
     * 添加菜谱食材关联数据
     *
     * @param setmealDishList 菜谱食材关联数据List集合
     */
    void batchInsertSetmealDish(@Param("setmealDishList") List<SetmealDish> setmealDishList);

    /**
     * 查询菜谱中包含的食材信息
     *
     * @param setmealId 菜谱Id
     * @return 菜谱中食材的基本信息
     */
    List<DishItemVO> selectSetmealDishBySetmealId(@Param("setmealId") Long setmealId);



}
