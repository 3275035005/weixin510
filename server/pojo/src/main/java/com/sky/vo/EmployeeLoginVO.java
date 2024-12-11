package com.sky.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 员工登录结果(响应数据)存储类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "员工登录返回的数据格式")
public class EmployeeLoginVO implements Serializable {
    //员工Id
    @ApiModelProperty("主键值")
    private Long id;
    //员工用户名
    @ApiModelProperty("用户名")
    private String userName;
    //员工真实姓名
    @ApiModelProperty("姓名")
    private String name;
    //Jwt令牌数据(下发给客户端之后每次请求会携带此Jwt令牌作为请求头)
    @ApiModelProperty("Jwt令牌")
    private String token;

    // 1管理员 2商家 3用户
    @ApiModelProperty("角色")
    private String role;
}
