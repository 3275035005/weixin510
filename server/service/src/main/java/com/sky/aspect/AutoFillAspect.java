package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: AutoFillAspect
 * USER: SHINIAN
 * DATE: 2023/5/17
 * DESCRIPTION : 字段自动填充的切面类
 */
@Slf4j
@Aspect //切面类
@Component //交给Spring容器管理
public class AutoFillAspect {
    //用于匹配所有标记了标记了AutoFill注解的方法
    @Pointcut("@annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {
    }

    /**
     * 字段自动填充(前置通知):当autoFillPointCut上的切点表达式匹配到的方法执行前默认执行前置通知
     * 解决问题(1):如何获取到要增强的方法的原始信息 【环绕通知:ProceedingJoinPoint】【非环绕通知:JoinPoint】
     * 解决问题(2):如何进行判断(本次要做的添加/更新) 基于JoinPoint提供的功能来获取到原始方法Method对象
     *
     * @param joinPoint 本次匹配到的原始方法的相关信息
     */
    @Before("autoFillPointCut()")
    public void beforeAutoFillMethod(JoinPoint joinPoint) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        log.info("【开始】 公共字段自动填充");
        //获取到原始方法的方法签名对象(包含了原始方法的相关信息)
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        //基于表示原始方法的Method对象获取头上标记的相关注解内容
        AutoFill autoFill = method.getDeclaredAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();
        //基于joinPoint获取本次原始方法的实际参数
        Object[] args = joinPoint.getArgs();
        if (Objects.isNull(args) || args.length == 0)
            return;
        Object targetEntity = args[0];
        //准备要赋的实际数据(当前时间/当前登录用户的Id)
        LocalDateTime now = LocalDateTime.now();
        Long employeeId = BaseContext.getCurrentId();
        //针对于本次匹配到的方法上标记的@AutoFill里的枚举内容(新增/更新)
        log.info("公共字段填充前原始方法参数对象的内容 : {}", targetEntity);
        switch (operationType) {
            case INSERT:
                //反射获取到用于给目标对象字段赋值的SET方法的Method对象
                Method iSetCreateTime = targetEntity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method iSetUpdateTime = targetEntity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method iSetCreateUser = targetEntity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method iSetUpdateUser = targetEntity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                iSetCreateTime.invoke(targetEntity, now);
                iSetUpdateTime.invoke(targetEntity, now);
                iSetCreateUser.invoke(targetEntity, employeeId);
                iSetUpdateUser.invoke(targetEntity, employeeId);
                break;
            case UPDATE:
                //反射获取到用于给目标对象字段赋值的SET方法的Method对象
                Method uSetUpdateTime = targetEntity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method uSetUpdateUser = targetEntity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                uSetUpdateTime.invoke(targetEntity, now);
                uSetUpdateUser.invoke(targetEntity, employeeId);
                break;
        }
        log.info("公共字段填充后原始方法参数对象的内容 : {}", targetEntity);
    }
}
