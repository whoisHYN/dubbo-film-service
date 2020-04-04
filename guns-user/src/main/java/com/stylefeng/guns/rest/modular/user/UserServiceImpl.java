package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.api.user.UserAPI;
import com.stylefeng.guns.api.user.vo.UserInfoModel;
import com.stylefeng.guns.api.user.vo.UserModel;
import com.stylefeng.guns.core.util.MD5Util;
import com.stylefeng.guns.rest.common.persistence.dao.UserTMapper;
import com.stylefeng.guns.rest.common.persistence.model.UserT;
import org.springframework.stereotype.Component;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/2 6:03 下午
 * @Modified By:
 */
@Component
@Service(interfaceClass = UserAPI.class)
public class UserServiceImpl implements  UserAPI {

    private final UserTMapper userTMapper;

    public UserServiceImpl(UserTMapper userTMapper) {
        this.userTMapper = userTMapper;
    }

    /**
     * 用户登录，返回uuid
     * @param username
     * @param password
     * @return
     */
    @Override
    public int login(String username, String password) {
        //封装对象查询数据库
        UserT userT = new UserT();
        userT.setUserName(username);
        UserT user = userTMapper.selectOne(userT);
        //获取结果并与加密后的数据作对比
        if (user != null && user.getUuid() > 0) {
            String encrypt = MD5Util.encrypt(password);
            if (user.getUserPwd().equals(encrypt)) {
                return user.getUuid();
            }
        }
        return 0;
    }

    /**
     * 注册
     * @param userModel
     * @return
     */
    @Override
    public boolean register(UserModel userModel) {
        //1. 获取登录信息，已经包含在UserModel里了
        //2. 将注册信息转换成数据库实体类MoocUserT
        //3. 将用户存入数据库
        UserT userT = new UserT();
        userT.setUserName(userModel.getUsername());
        userT.setAddress(userModel.getAddress());
        userT.setUserPhone(userModel.getPhone());
        userT.setEmail(userModel.getEmail());
        //将密码md5加密在存入数据库
        String encrypt = MD5Util.encrypt(userModel.getPassword());
        userT.setUserPwd(encrypt);

        Integer insert = userTMapper.insert(userT);
        return insert > 0;
    }

    /**
     * 检查用户名是否已存在
     * @param username
     * @return
     */
    @Override
    public boolean checkUsername(String username) {
        EntityWrapper<UserT> wrapper = new EntityWrapper<>();
        wrapper.eq("user_name", username);
        Integer result = userTMapper.selectCount(wrapper);
        // 有结果说明该用户名已存在，返回false
        return result == null || result <= 0;
    }

    private UserInfoModel do2UserInfoModel(UserT userT) {
        UserInfoModel model = new UserInfoModel();
        // 填充数据
        model.setUuid(userT.getUuid());
        model.setUsername(userT.getUserName());
        model.setUpdateTime(userT.getUpdateTime().getTime());
        model.setSex(userT.getUserSex());
        model.setPhone(userT.getUserPhone());
        model.setNickname(userT.getNickName());
        model.setLifeState(userT.getLifeState() + "");
        model.setHeadAddress(userT.getHeadUrl());
        model.setEmail(userT.getEmail());
        model.setBirthday(userT.getBirthday());
        model.setBiography(userT.getBiography());
        model.setBeginTime(userT.getBeginTime().getTime());
        model.setAddress(userT.getAddress());

        return model;
    }

    @Override
    public UserInfoModel getUserInfo(int uuid) {
        UserT userT = userTMapper.selectById(uuid);
        return do2UserInfoModel(userT);
    }

    @Override
    public UserInfoModel updateUserInfo(UserInfoModel userInfoModel) {
        // 将传入的数据z转换成MoocUserT
        UserT userT = new UserT();

        userT.setUuid(userInfoModel.getUuid());
        userT.setNickName(userInfoModel.getNickname());
        userT.setLifeState(Integer.parseInt(userInfoModel.getLifeState()));
        userT.setBirthday(userInfoModel.getBirthday());
        userT.setBiography(userInfoModel.getBiography());
        //moocUserT.setBeginTime(null);
        userT.setHeadUrl(userInfoModel.getHeadAddress());
        userT.setEmail(userInfoModel.getEmail());
        userT.setAddress(userInfoModel.getAddress());
        userT.setUserPhone(userInfoModel.getPhone());
        userT.setUserSex(userInfoModel.getSex());
        //moocUserT.setUpdateTime(null);

        //存入数据库
        Integer result = userTMapper.updateById(userT);
        if (result > 0) {
            return getUserInfo(userT.getUuid());
        }
        return userInfoModel;
    }
}
