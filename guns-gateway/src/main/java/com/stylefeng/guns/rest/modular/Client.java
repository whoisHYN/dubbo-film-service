package com.stylefeng.guns.rest.modular;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.user.UserAPI;
import org.springframework.stereotype.Component;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/2 10:54 下午
 * @Modified By:
 */
@Component
public class Client {

    @Reference(interfaceClass = UserAPI.class)
    private UserAPI userAPI;

    public void run() {
        userAPI.login("admin", "password");
    }
}
