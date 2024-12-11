package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: ShopController
 * DESCRIPTION : B端-店铺信息的表现层
 */
@Slf4j
@Api(tags = "B端-店铺信息表现层")
@RequestMapping("/admin/shop")
@RestController
public class ShopController {

    //注入RedisTemplate
    @Resource
    private RedisTemplate redisTemplate;

    //由于修改店铺状态/获取店铺状态都要该KEY,抽取出来!
    private final static String SHOP_STATUS = "SHOP_STATUS";

    /**
     * 修改店铺状态
     *
     * @param status 修改后的状态 1:营业 0:打烊
     * @return 全局通用返回信息Bean
     */
    @ApiOperation("B端-修改店铺状态")
    @PutMapping("/{status}")
    public Result modifyShopStatus(@PathVariable Integer status) {
        log.info("店铺状态进行修改,修改后状态 : {}", status == 1 ? "营业中" : "打烊中");
        //通过RedisTemplate模板获取操作String信息的操作类后进行存储
        redisTemplate.opsForValue().set(SHOP_STATUS, status);
        return Result.success();
    }

    /**
     * 获取店铺状态
     *
     * @return 全局通用返回Bean(店铺状态码 1 : 营业 0 : 打烊)
     */
    @ApiOperation("B端-获取店铺状态")
    @GetMapping("/status")
    public Result<Integer> selectShopStatus() {
        //通过RedisTemplate模板获取操作String信息的操作类后进行查询
        System.out.println(redisTemplate.opsForValue().get(SHOP_STATUS));
//        log.info("店铺状态进行获取,状态是 : {}", shopStatus == 1 ? "营业中" : "打烊中");
        return Result.success(1);
    }
}
