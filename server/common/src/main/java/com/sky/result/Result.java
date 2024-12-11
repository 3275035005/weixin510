package com.sky.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 后端统一返回结果
 * @param <T>
 */
@Data
public class Result<T> implements Serializable {

    private Integer code; //结果代码 成功:0 其它数字为失败
    private String msg; //相关信息
    private T data; //数据

    //快速返回一个表示正确结果的Result对象(无数据封装)
    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.code = 1;
        return result;
    }

    //快速返回一个表示正确结果的Result对象(有数据封装)
    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<T>();
        result.data = object;
        result.code = 1;
        return result;
    }

    //快速返回一个表示错误结果的Result对象(无数据封装)
    public static <T> Result<T> error(String msg) {
        Result result = new Result();
        result.msg = msg;
        result.code = 0;
        return result;
    }

}
