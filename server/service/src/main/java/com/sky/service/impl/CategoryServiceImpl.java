package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: CategoryServiceImpl
 * DESCRIPTION : B端-分类信息服务层接口实现类
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    //注入分类持久层接口实现Bean/食材持久层接口实现Bean/菜谱持久层接口实现Bean
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private DishMapper dishMapper;
    @Resource
    private SetmealMapper setmealMapper;

    /**
     * 添加分类信息
     *
     * @param categoryDTO 分类基本信息Bean
     */
    @Override
    public void insertCategory(CategoryDTO categoryDTO) {
        //创建分类信息Bean并将参数Bean的内容进行拷贝转移
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        //封装基本信息外的其他内容(默认分类状态为【禁用】)
        category.setStatus(StatusConstant.DISABLE);
        categoryMapper.insertCategory(category);
    }

    /**
     * 分页查询分类功能
     *
     * @param categoryPageQueryDTO 分页参数Bean(查询页数/每页显示条数/分类名称/分类类型)
     * @return 分页结果封装Bean
     */
    @Override
    public PageResult selectByPage(CategoryPageQueryDTO categoryPageQueryDTO) {
        //开启分页查询拦截
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        Page<Category> categoryPage = categoryMapper.selectByPage(categoryPageQueryDTO);
        return new PageResult(categoryPage.getTotal(), categoryPage.getResult());
    }

    /**
     * 基于分类Id删除分类
     *
     * @param categoryId 要删除的分类Id
     */
    @Override
    public void deleteCategoryById(Long categoryId) {
        //校验1:当前分类下是否关联了其他食材信息
        if (dishMapper.selectDishCountByCategoryId(categoryId) != 0)
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        //校验2:当前分类是否关联了其他菜谱信息
        if (setmealMapper.selectSetmealCountByCategoryId(categoryId) != 0)
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        //通过校验则删除分类信息
        categoryMapper.deleteCategoryById(categoryId);
    }

    /**
     * 更新分类信息
     *
     * @param categoryDTO 更新后的分类基本信息(基于Id)
     */
    @Override
    public void updateCategoryById(CategoryDTO categoryDTO) {
        //创建分类信息Bean并将参数Bean的内容进行转移
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        categoryMapper.updateCategoryById(category);
    }

    /**
     * 修改分类状态
     *
     * @param categoryId 要修改的分类状态Id
     * @param status     修改后的状态
     */
    @Override
    public void modifyStatus(Long categoryId, Integer status) {
        //创建分类信息Bean并将Id与修改后状态以及更新信息封装到Bean中
        Category newCategory = Category.builder().id(categoryId).status(status).updateTime(LocalDateTime.now()).updateUser(BaseContext.getCurrentId()).build();
        categoryMapper.updateCategoryById(newCategory);
    }

    /**
     * 查询指定类型的分类信息
     *
     * @param type 分类类型(食材分类/菜谱分类)
     * @return 对应分类类型的分类信息
     */
    @Override
    public List<Category> selectByType(Integer type) {
        return categoryMapper.selectByType(type);
    }
}
