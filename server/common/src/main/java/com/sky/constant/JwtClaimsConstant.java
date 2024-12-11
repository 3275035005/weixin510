package com.sky.constant;

/**
 * JWT令牌部分(2)-令牌内容字段声明(方便从令牌的Claims双列集合中获取数据)
 */
public class JwtClaimsConstant {

    public static final String EMP_ID = "empId"; //Claims中的KEY(员工Id)
    public static final String USER_ID = "userId"; //Claims中的KEY(用户Id)
    public static final String PHONE = "phone"; //Claims中的KEY(手机号码)
    public static final String USERNAME = "username"; //Claims中的KEY(登录名称)
    public static final String NAME = "name"; //Claims中的KEY(实际名称)

}
