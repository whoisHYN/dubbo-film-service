package com.stylefeng.guns.api.user;

import com.stylefeng.guns.api.user.vo.UserInfoModel;
import com.stylefeng.guns.api.user.vo.UserModel;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/2 6:02 下午
 * @Modified By:
 */
public interface UserAPI {
    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    int login(String username, String password);

    /**
     * 用户登录
     * @param userModel
     * @return
     */
    boolean register(UserModel userModel);

    /**
     * 检查用户名
     * @param username
     * @return
     */
    boolean checkUsername(String username);

    /**
     * 获取用户信息
     * @param uuid
     * @return
     */
    UserInfoModel getUserInfo(int uuid);

    /**
     * 更新用户信息
     * @param userInfoModel
     * @return
     */
    UserInfoModel updateUserInfo(UserInfoModel userInfoModel);
}
