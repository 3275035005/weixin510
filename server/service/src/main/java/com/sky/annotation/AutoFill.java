package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * PROJECT_NAME: sky-take-out
 * NAME: AutoFill
 * DESCRIPTION : 标记持久层方法(声明字段自动填充)
 */
@Target(ElementType.METHOD) //只可以标记在方法上
@Retention(RetentionPolicy.RUNTIME) //存活到运行期
public @interface AutoFill {
    //当使用@AutoFill标记方法的时候,必须给value赋值(@AutoFill(OperationType.INSERT))
    OperationType value();
}
