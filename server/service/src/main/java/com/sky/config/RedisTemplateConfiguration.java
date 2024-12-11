package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * PROJECT_NAME: sky-take-out-everyday
 * NAME: RedisTemplateConfiguration
 * USER: SHINIAN
 * DATE: 2023/5/20
 * DESCRIPTION : 用于将RedisTemplate注入到IOC容器中的配置类
 */
@Slf4j
@Configuration
public class RedisTemplateConfiguration {

    /**
     * 注入RedisTemplate到IOC容器
     *
     * @param redisConnectionFactory Redis连接工厂(要操作Redis也要先获取连接)
     * @return RedisTemplate:操作模板类
     */
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("【开始】 Redis操作模板Bean开始初始化..");
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory); //声明连接工厂
        redisTemplate.setKeySerializer(new StringRedisSerializer()); //声明使用指定的序列化器
        return redisTemplate;
    }
}
