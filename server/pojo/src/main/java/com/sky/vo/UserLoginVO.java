package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户登录详情(响应数据)存储类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginVO implements Serializable {
    //用户Id
    private Long id;
    //用户微信OPEN_ID
    private String openid;
    //Jwt令牌(下发给客户端候之后每次请求会携带此Jwt令牌作为请求头)
    private String token;
}
