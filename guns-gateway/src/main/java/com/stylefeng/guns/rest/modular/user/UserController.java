package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.user.UserAPI;
import com.stylefeng.guns.api.user.UserInfoModel;
import com.stylefeng.guns.api.user.UserModel;
import com.stylefeng.guns.rest.common.CurrentUser;
import com.stylefeng.guns.rest.modular.vo.ResponseVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/3 8:19 下午
 * @Modified By:
 */
@RestController
@RequestMapping("/user/")
public class UserController {

    @Reference(interfaceClass = UserAPI.class, check = false)
    private UserAPI userAPI;

    @PostMapping("register")
    public ResponseVO<?> register(UserModel userModel) {
        if (userModel.getUsername() == null || userModel.getUsername().trim().length() == 0) {
            return ResponseVO.serviceFail("用户名不能为空");
        }
        if (userModel.getPassword() == null || userModel.getPassword().trim().length() == 0) {
            return ResponseVO.serviceFail("密码不能为空");
        }
        boolean isSuccess = userAPI.register(userModel);
        if (isSuccess) {
            return ResponseVO.success("注册成功！");
        } else {
            return ResponseVO.serviceFail("注册失败");
        }
    }

    @PostMapping("check")
    public ResponseVO<?> check(String username) {
        if (username != null && username.trim().length() != 0) {
            // true表示用户名可用
            boolean notExists = userAPI.checkUsername(username);
            return notExists ? ResponseVO.success("用户名不存在") : ResponseVO.serviceFail("用户名已存在");
        } else {
            return ResponseVO.serviceFail("用户名不能为空");
        }
    }

    @GetMapping("logout")
    public ResponseVO<?> logout() {
        /*
            企业及开发此功能的实现细节：
            应用：
                1、前端存储JWT 【七天】 ： JWT的刷新
                2、服务器端会存储活动用户信息【30分钟】
                3、JWT里的userId为key，查找活跃用户
            退出：
                1、前端删除掉JWT
                2、后端服务器删除活跃用户缓存
            现状：
                1、前端删除掉JWT
         */
        // 由于本系统没有redis等缓存中间件，故不能实现上述功能
        return ResponseVO.success("用户退出成功");
    }

    @GetMapping("getUserInfo")
    public ResponseVO<UserInfoModel> getUserInfo() {
        String userId = CurrentUser.getUser();
        if (userId != null && userId.trim().length() != 0) {
            UserInfoModel userInfo = userAPI.getUserInfo(Integer.parseInt(userId));
            if (userInfo != null) {
                return ResponseVO.success(userInfo);
            } else {
                return ResponseVO.serviceFail("用户信息查询失败");
            }
        } else {
            return ResponseVO.serviceFail("用户未登录");
        }
    }

    @PostMapping("updateUserInfo")
    public ResponseVO<?> updateUserInfo(UserInfoModel userInfoModel) {
        String userId = CurrentUser.getUser();
        if (userId != null && userId.trim().length() != 0) {
            //先判断是不是本人在修改
            int uuid = Integer.parseInt(userId);
            if (uuid != userInfoModel.getUuid()) {
                return ResponseVO.serviceFail("只能修改自己的信息");
            }
            UserInfoModel userInfo = userAPI.updateUserInfo(userInfoModel);
            if (userInfo != null) {
                return ResponseVO.success(userInfo);
            } else {
                return ResponseVO.serviceFail("用户信息查询失败");
            }
        } else {
            return ResponseVO.serviceFail("用户未登录");
        }
    }
}
