package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器(当出现了未处理的运行期/编译期异常最终由此类中的方法进行处理)
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 如果这里写Exception则范围过大,任何编译期与运行期异常都会按照此方法进行处理。
     * 不能针对于唯一索引冲突异常额外给出处理方案！
     * 自己定义了一个BaseException(extend RuntimeExpcetion) => 所有运行期异常按照此方法进行处理!
     *
     * @param e 出现的异常对象
     * @return 全局通用返回Bean(包含一些错误的提示信息)
     */
    @ExceptionHandler(BaseException.class)
    public Result exceptionHandler(BaseException e) {
        //toString : 异常对象变成字符串(异常的全限定类名:异常的提示信息) getMessage : 获取异常对象中的提示信息
        log.error("异常处理器捕获到了未处理的异常,异常信息是 : {}", e.toString());
        return Result.error(e.getMessage());
    }

    /**
     * 针对于SQL语句添加唯一索引冲突的异常给一套处理方案 SQLIntegrityConstraintViolationException
     *
     * @param e 出现的异常对象
     * @return 全局通用返回Bean(包含一些错误的提示信息)
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result mySQLUniqueIndexExceptionHandler(SQLIntegrityConstraintViolationException e) {
        log.error("异常处理器捕获到了未处理的异常,异常信息是 : {}", e.toString());
        if (e.getMessage().contains("Duplicate entry")) {
            String duplicateEntry = e.getMessage().split(" ")[2].replace("'", "");//Duplicate entry 'zhangergou' for key 'idx_username'
            return Result.error("当前项" + duplicateEntry + "已存在~ 请修改后重新尝试!");
        }
        return Result.error(MessageConstant.UNKNOWN_ERROR);
    }

}
