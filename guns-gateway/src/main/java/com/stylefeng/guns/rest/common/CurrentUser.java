package com.stylefeng.guns.rest.common;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/3 1:21 下午
 * @Modified By:
 */
public class CurrentUser {
    /**
     * 线程绑定的存储空间
     */
    private static final InheritableThreadLocal<String> THREAD_LOCAL = new InheritableThreadLocal<>();

    public static void setUser(String userId) {
        THREAD_LOCAL.set(userId);
    }

    public static String getUser() {
        String s = THREAD_LOCAL.get();
        //手动remove防止内存泄漏
        THREAD_LOCAL.remove();
        return s;
    }
}
