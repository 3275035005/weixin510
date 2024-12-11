package com.sky.controller.admin;

import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.dto.SharePageQueryDTO;
import com.sky.entity.Share;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.ShareService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: ShareController
 * DESCRIPTION : B端-美食社区表现层接口
 */
@Slf4j
@Api(tags = "B端-美食社区表现层接口")
@RestController
@RequestMapping("/admin/share")
public class ShareController {
    //注入美食社区服务层接口实现类对象
    @Resource
    private ShareService ShareService;


    /**
     * 分页查询美食社区信息功能
     *
     * @param sharePageQueryDTO 分页参数Bean(查询页数/每页显示条数)
     * @return 全局通用信息返回Bean(分页查询结果Bean)
     */
    @GetMapping("/page")
    public Result<PageResult> selectByPage(SharePageQueryDTO sharePageQueryDTO) {
        //调用服务层完成数据查询获取分页查询结果对象并返回
        PageResult pageResult = ShareService.selectByPage(sharePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 基于Id审核状态
     *
     * @param id Id
     * @return 全局通用信息返回Bean
     */
    @ApiOperation("B端-基于Id审核状态")
    @PutMapping("/{id}/{status}")
    public Result updateById(@PathVariable Long id,@PathVariable String status) {
        //调用服务层完成分类信息的更新
        ShareService.updateShareStatus(id, status);
        return Result.success();
    }
}
