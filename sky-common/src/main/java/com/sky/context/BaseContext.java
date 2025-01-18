package com.sky.context;

/**
 * 一些特定情境下，我们无法通过request.getSession.getAttribute获取当前所登录用户的id，
 *  此时若需要获取当前所登录用户的id，我们可以采用ThreadLocal来解决问题
 */
public class BaseContext {

    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }

    public static void removeCurrentId() {
        threadLocal.remove();
    }

}
