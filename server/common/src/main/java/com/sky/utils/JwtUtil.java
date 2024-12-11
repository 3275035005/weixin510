package com.sky.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * Jwt令牌工具类 用于将共享数据以及秘钥生成令牌和解析令牌
 * eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoi5byg5LqM54uXIiwiaWQiOjE4LCJ1c2VybmFtZSI6InpoYW5nZXJnb3UiLCJleHAiOjE2ODM1ODE3MDR9.5jtSCftvGMnt38l7qUPhE6kVtqK5SjwXsBmj0L7Bqv4
 * Jwt令牌解析分析:Jwt令牌字符串用.进行分隔,分成三个部分
 * 部分(1)加密的方式 BASE64编码 部分(2)令牌数据 本质是一个Map集合的JSON 通过BASE64编码 部分(3)加密数据 基于部分(1)+部分(2)+秘钥通过指定加密方式加密后的字符串
 */
public class JwtUtil {

    /**
     * 生成Jwt令牌功能
     *
     * @param secretKey 秘钥数据 用于生成部分(3)内容
     * @param ttlMillis 令牌过期时间毫秒值
     * @param claims 令牌部分(2)数据
     * @return 令牌内容
     */
    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; //声明签名算法
        long expMillis = System.currentTimeMillis() + ttlMillis; //声明令牌过期时间点
        Date exp = new Date(expMillis);
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims) //声明部分(2)令牌内部
                .signWith(signatureAlgorithm, secretKey.getBytes(StandardCharsets.UTF_8)) //声明签名方式与秘钥
                .setExpiration(exp); //声明过期时间
        return builder.compact(); //生成令牌返回
    }

    /**
     * 解析Jwt令牌
     * @param secretKey 秘钥数据
     * @param token Jwt令牌数据
     * @return 解析令牌后的部分(2)内容
     */
    public static Claims parseJWT(String secretKey, String token) {
        return Jwts.parser()
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8)) //声明秘钥数据用于校验部分(3)内容 此处如果令牌非法/超过了有效时间则抛出异常
                .parseClaimsJws(token).getBody(); //声明要解析的Token令牌并返回解析后的部分(2)解码数据
    }

}
