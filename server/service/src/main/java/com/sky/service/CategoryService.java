package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: CategoryService
 * DESCRIPTION : B端-分类信息服务层接口
 */
public interface CategoryService {

    /**
     * 添加分类信息
     *
     * @param categoryDTO 分类基本信息Bean
     */
    void insertCategory(CategoryDTO categoryDTO);

    /**
     * 分页查询分类功能
     *
     * @param categoryPageQueryDTO 分页参数Bean(查询页数/每页显示条数/分类名称/分类类型)
     * @return 分页结果封装Bean
     */
    PageResult selectByPage(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 基于分类Id删除分类
     *
     * @param categoryId 要删除的分类Id
     */
    void deleteCategoryById(Long categoryId);

    /**
     * 更新分类信息
     *
     * @param categoryDTO 更新后的分类基本信息(基于Id)
     */
    void updateCategoryById(CategoryDTO categoryDTO);

    /**
     * 修改分类状态
     *
     * @param categoryId 要修改的分类状态Id
     * @param status     修改后的状态
     */
    void modifyStatus(Long categoryId, Integer status);

    /**
     * 查询指定类型的分类信息
     *
     * @param type 分类类型(食材分类/菜谱分类)
     * @return 对应分类类型的分类信息
     */
    List<Category> selectByType(Integer type);
}
