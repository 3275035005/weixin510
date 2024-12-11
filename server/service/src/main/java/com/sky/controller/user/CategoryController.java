package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: CategoryController
 * DESCRIPTION : C端-分类信息表现层接口
 */
@Slf4j
@Api(tags = "C端-分类信息表现层接口")
@RequestMapping("/user/category")
@RestController("userCategoryController") //防止与B端的CategoryController在容器中的名称冲突
public class CategoryController {
    //注入分类信息服务层接口实现类对象
    @Resource
    private CategoryService categoryService;

    /**
     * 查询所有可用分类信息
     *
     * @param type 分类类型(可能为NULL,如果是NULL则表示查询所有分类)
     * @return 全局通用返回信息Bean(分类List集合)
     */
    @ApiOperation("C端-查询所有可用分类信息")
    @GetMapping("/list")
    public Result<List<Category>> selectCategoryByType(Integer type) {
        log.info("本次查询的分类类型是 : {}", type);
        //调用服务层查询所有分类信息
        List<Category> categoryList = categoryService.selectByType(type);
        return Result.success(categoryList);
    }
}
