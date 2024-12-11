package com.sky.interceptor;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jwt令牌拦截器(商家端)
 */
@Slf4j
@Component
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    //注入Jwt令牌信息参数封装Bean(包含秘钥)
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 拦截器拦截到请求执行的功能
     *
     * @param request  本次拦截到的请求的请求对象
     * @param response 本次拦截到的请求的响应对象
     * @param handler  本次拦截到的请求要访问的方法
     * @return 是否放行 true:放行 false:不放行
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法,直接放行
            return true;
        }
        //从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getAdminTokenName());
        //对令牌进行校验
        try {
            log.info("Jwt令牌拦截器(商家端)开始对令牌进行校验 : {}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
            log.info("Jwt令牌拦截器(商家端)校验成功,获取到的员工Id : {}", empId);
            //将本次获取到的员工Id放到线程变量ThreadLocal中(基于BaseContext进行存储)
            BaseContext.setCurrentId(empId);
            return true; //放行
        } catch (Exception ex) {
            //如果在校验令牌的过程中出现异常(令牌过期/令牌非法) 通过响应对象响应状态码401(401错误代表用户没有访问权限)
            response.setStatus(401);
            return false; //拦截请求
        }
    }
}