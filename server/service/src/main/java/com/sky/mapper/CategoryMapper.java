package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: CategoryMapper

 * DESCRIPTION : B端-分类信息持久层接口
 */
@Mapper
public interface CategoryMapper {
    /**
     * 添加分类信息
     *
     * @param category 分类信息Bean
     */
    @AutoFill(OperationType.INSERT)
    void insertCategory(@Param("category") Category category);

    /**
     * 分页查询分类功能
     *
     * @param categoryPageQueryDTO 分页参数Bean(查询页数/每页显示条数/分类名称/分类类型)
     * @return 分页结果封装Bean
     */
    Page<Category> selectByPage(@Param("categoryPageQueryDTO") CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 基于分类Id删除分类
     *
     * @param categoryId 要删除的分类Id
     */
    void deleteCategoryById(@Param("categoryId") Long categoryId);

    /**
     * 更新分类信息
     *
     * @param category 分类信息Bean 更新后的分类信息(基于Id)
     */
    @AutoFill(OperationType.UPDATE)
    void updateCategoryById(@Param("category") Category category);

    /**
     * 查询指定类型的分类信息
     *
     * @param type 分类类型(食材分类/菜谱分类)
     * @return 对应分类类型的分类信息
     */
    List<Category> selectByType(@Param("type") Integer type);

    /**
     * 基于分类Id查询分类名称
     * @param categoryId 分类Id
     * @return 分类名称
     */
    String selectCategoryNameById(@Param("categoryId") Long categoryId);
}
