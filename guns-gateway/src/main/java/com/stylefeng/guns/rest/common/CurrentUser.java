package com.stylefeng.guns.rest.common;

import com.stylefeng.guns.api.user.UserInfoModel;

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
    private static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void setUser(String userId) {
        threadLocal.set(userId);
    }

    public static String getUser() {
        return threadLocal.get();
    }
}
