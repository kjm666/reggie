package com.itheima.reggie.common;

/**
 * description: 基于ThreadLocal封装的工具类，用于获取当前用户的id
 *
 * @since: 1.0.0
 * @author: KangJiaMing
 * @date: 2023/3/15 19:54
 */
public class BaseContext {
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }


}
