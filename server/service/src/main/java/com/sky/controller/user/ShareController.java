package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.Share;
import com.sky.result.Result;
import com.sky.service.ShareService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.BagUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: ShareController
 * DESCRIPTION : C端-美食社区表现层接口
 */
@Slf4j
@Api(tags = "C端-美食社区表现层接口")
@RequestMapping("/user/share")
@RestController("userShareController") //防止与B端的ShareController在容器中的名称冲突
public class ShareController {
    //注入美食社区服务层接口实现类对象
    @Resource
    private ShareService ShareService;

    /**
     * 查询我的美食分享
     * @param type : 0查询所有状态审核通过 1查询我的发布
     * @return 全局通用返回信息Bean(收藏List集合)
     */
    @ApiOperation("C端美食社区")
    @GetMapping("/list")
    public Result<List<Share>> list(String type) {
        Share share = new Share();
        if("0".equals(type)){
            share.setStatus("1");
        }else{
            share.setEmployeeId(BaseContext.getCurrentId());
        }
        List<Share> list = ShareService.selectList(share);
        return Result.success(list);
    }

    /**
     * 发布美食
     * @return
     */
    @PostMapping("/insertShare")
    public Result<Long> insertShare(@RequestBody Share Share) {
        ShareService.insertShare(Share);
        return Result.success(Share.getId());
    }


    /**
     * 查询美食详情
     * @return
     */
    @GetMapping("/getShareById")
    public Result getShareById(Long shareId) {
        Share share = ShareService.selectShareById(shareId);
        return Result.success(share);
    }
}
