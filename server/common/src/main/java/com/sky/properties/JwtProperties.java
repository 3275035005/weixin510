package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 用于保存JWT令牌参数的Bean
 * 会在项目启动的时候自动会application.yml中sky.jwt的信息自动注入到对象中并放到IOC容器中
 */
@Data
@Component
@ConfigurationProperties(prefix = "sky.jwt")
public class JwtProperties {
    /*----------------B端(管理端)-令牌生成方案----------------*/
    private String adminSecretKey; //用于生成部分(3)的加密秘钥
    private long adminTtl; //过期时间(毫秒值)
    private String adminTokenName; //前台发送请求的请求头名称(用于在拦截器从Request对象中获取Jwt令牌)
    /*----------------B端(管理端-令牌生成方案----------------*/

    /*----------------C端(客户端)-令牌生成方案----------------*/
    private String userSecretKey; //用于生成部分(3)的加密秘钥
    private long userTtl; //过期时间(毫秒值)
    private String userTokenName; //前台发送请求的请求头名称(用于在拦截器从Request对象中获取Jwt令牌)
    /*----------------C端(客户端)-令牌生成方案----------------*/
}
