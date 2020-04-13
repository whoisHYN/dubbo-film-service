package com.stylefeng.guns.core.util;

import java.util.UUID;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/12 9:04 上午
 * @Modified By:
 */
public class UUIDUtil {
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
