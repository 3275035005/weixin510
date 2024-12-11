package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: CategoryController
 * DESCRIPTION : B端-分类信息表现层接口
 */
@Slf4j
@Api(tags = "B端-分类表现层接口")
@RestController
@RequestMapping("/admin/category")
public class CategoryController {

    //注入分类服务层接口实现Bean
    @Resource
    private CategoryService categoryService;

    /**
     * 分页查询分类信息功能
     *
     * @param categoryPageQueryDTO 分页参数Bean(查询页数/每页显示条数/分类名称/分类类型)
     * @return 全局通用信息返回Bean(分页查询结果Bean)
     */
    @ApiOperation("B端-分类信息分页查询")
    @GetMapping("/page")
    public Result<PageResult> selectByPage(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("分类信息分页查询功能的参数信息 : {}", categoryPageQueryDTO);
        //调用服务层完成数据查询获取分页查询结果对象并返回
        PageResult pageResult = categoryService.selectByPage(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 添加分类信息功能
     *
     * @param categoryDTO 分类添加数据数据传输类
     * @return 全局通用信息返回Bean
     */
    @ApiOperation("B端-分类信息添加")
    @PostMapping
    public Result insertCategory(@RequestBody CategoryDTO categoryDTO) {
        log.info("添加分类信息的参数信息 : {}", categoryDTO);
        //调用服务层完成分类数据添加
        categoryService.insertCategory(categoryDTO);
        return Result.success();
    }

    /**
     * 查询指定类型的所有分类信息功能
     *
     * @param type 类型Id(1:食材分类 2:菜谱分类)
     * @return 全局通用信息返回Bean(对应分类类型的信息List集合)
     */
    @ApiOperation("B端-基于分类类型查询分类信息")
    @GetMapping("/list")
    public Result<List<Category>> selectByType(Integer type) {
        log.info("基于分类类型查询分类信息的参数信息 : {}", type);
        //调用服务层查询对应分类类型的List集合数据并返回
        List<Category> categoryList = categoryService.selectByType(type);
        return Result.success(categoryList);
    }

    /**
     * 基于Id删除分类信息功能
     *
     * @param id 要删除的分类Id
     * @return 全局通用信息返回Bean
     */
    @ApiOperation("B端-基于分类Id删除分类信息")
    @DeleteMapping
    public Result deleteById(Long id) {
        log.info("基于Id删除分类信息的参数信息 : {}", id);
        //调用服务层删除对应Id的分类信息
        categoryService.deleteCategoryById(id);
        return Result.success();
    }

    /**
     * 基于Id更新分类信息功能
     *
     * @param categoryDTO 分类基本信息(包含Id)
     * @return 全局通用信息返回Bean
     */
    @ApiOperation("B端-基于Id更新分类信息")
    @PutMapping
    public Result updateById(@RequestBody CategoryDTO categoryDTO) {
        log.info("更新分类信息的参数信息 : {}", categoryDTO);
        //调用服务层完成分类信息的更新
        categoryService.updateCategoryById(categoryDTO);
        return Result.success();
    }

    /**
     * 基于分类Id修改分类状态功能
     *
     * @param status 修改后分类状态(状态码 0:禁用 1:启用)
     * @param id     分类Id
     * @return 全局通用信息返回Bean
     */
    @ApiOperation("B端-基于Id修改状态信息")
    @PostMapping("/status/{status}")
    public Result modifyStatus(@PathVariable Integer status, Long id) {
        log.info("更新分类信息状态的参数信息分类Id : {},更新状态 : {}", id, status);
        //调用服务层完成分类信息的状态更新
        categoryService.modifyStatus(id, status);
        return Result.success();
    }
}
