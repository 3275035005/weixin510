package com.sky.context;

/**
 * 基础环境抽象Bean
 */
public class BaseContext {

    //声明线程变量
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    //将员工/用户Id填充到线程变量中(全局可以获取)
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    //获取员工/用户Id
    public static Long getCurrentId() {
        return threadLocal.get();
    }

    //从线程变量中删除员工/用户Id
    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
