package com.sky.controller.user;

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
 * DESCRIPTION : C端-店铺信息的表现层
 */
@Slf4j
@Api(tags = "C端-店铺信息表现层")
@RequestMapping("/user/shop")
@RestController("userShopController") //避免和B端的ShopController冲突
public class ShopController {

    //注入RedisTemplate
    @Resource
    private RedisTemplate redisTemplate;

    //由于修改店铺状态/获取店铺状态都要该KEY,抽取出来!
    private final static String SHOP_STATUS = "SHOP_STATUS";

    /**
     * 获取店铺状态
     *
     * @return 全局通用返回Bean(店铺状态码 1 : 营业 0 : 打烊)
     */
    @ApiOperation("C端-获取店铺状态")
    @GetMapping("/status")
    public Result<Integer> selectShopStatus() {
        System.out.println(redisTemplate.opsForValue().get(SHOP_STATUS));
        //通过RedisTemplate模板获取操作String信息的操作类后进行查询
        Integer shopStatus = (Integer) redisTemplate.opsForValue().get(SHOP_STATUS);
        log.info("店铺状态进行获取,状态是 : {}", shopStatus == 1 ? "营业中" : "打烊中");
        return Result.success(shopStatus);
    }
}
