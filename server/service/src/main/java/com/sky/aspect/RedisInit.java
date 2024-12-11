package com.sky.aspect;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Order(1)
public class RedisInit implements CommandLineRunner {


    //注入RedisTemplate
    @Resource
    private RedisTemplate redisTemplate;

    //由于修改店铺状态/获取店铺状态都要该KEY,抽取出来!
    private final static String SHOP_STATUS = "SHOP_STATUS";

    @Override
    public void run(String... args){
        redisTemplate.opsForValue().set(SHOP_STATUS, 1);
    }
}
