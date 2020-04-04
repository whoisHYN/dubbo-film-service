package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.api.user.UserAPI;
import com.stylefeng.guns.api.user.UserInfoModel;
import com.stylefeng.guns.api.user.UserModel;
import com.stylefeng.guns.core.util.MD5Util;
import com.stylefeng.guns.rest.common.persistence.dao.MoocUserTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocUserT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author: HYN
 * @Description:
 * @Date: 2020/4/2 6:03 下午
 * @Modified By:
 */
@Component
@Service(interfaceClass = UserAPI.class)
public class UserServiceImpl implements  UserAPI {

    private final MoocUserTMapper moocUserTMapper;

    public UserServiceImpl(MoocUserTMapper moocUserTMapper) {
        this.moocUserTMapper = moocUserTMapper;
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
        MoocUserT moocUserT = new MoocUserT();
        moocUserT.setUserName(username);
        MoocUserT user = moocUserTMapper.selectOne(moocUserT);
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
        MoocUserT moocUserT = new MoocUserT();
        moocUserT.setUserName(userModel.getUsername());
        moocUserT.setAddress(userModel.getAddress());
        moocUserT.setUserPhone(userModel.getPhone());
        moocUserT.setEmail(userModel.getEmail());
        //将密码md5加密在存入数据库
        String encrypt = MD5Util.encrypt(userModel.getPassword());
        moocUserT.setUserPwd(encrypt);

        Integer insert = moocUserTMapper.insert(moocUserT);
        return insert > 0;
    }

    /**
     * 检查用户名是否已存在
     * @param username
     * @return
     */
    @Override
    public boolean checkUsername(String username) {
        EntityWrapper<MoocUserT> wrapper = new EntityWrapper<>();
        wrapper.eq("user_name", username);
        Integer result = moocUserTMapper.selectCount(wrapper);
        // 有结果说明该用户名已存在，返回false
        return result == null || result <= 0;
    }

    private UserInfoModel do2UserInfoModel(MoocUserT moocUserT) {
        UserInfoModel model = new UserInfoModel();
        // 填充数据
        model.setUuid(moocUserT.getUuid());
        model.setUsername(moocUserT.getUserName());
        model.setUpdateTime(moocUserT.getUpdateTime().getTime());
        model.setSex(moocUserT.getUserSex());
        model.setPhone(moocUserT.getUserPhone());
        model.setNickname(moocUserT.getNickName());
        model.setLifeState(moocUserT.getLifeState() + "");
        model.setHeadAddress(moocUserT.getHeadUrl());
        model.setEmail(moocUserT.getEmail());
        model.setBirthday(moocUserT.getBirthday());
        model.setBiography(moocUserT.getBiography());
        model.setBeginTime(moocUserT.getBeginTime().getTime());
        model.setAddress(moocUserT.getAddress());

        return model;
    }

    @Override
    public UserInfoModel getUserInfo(int uuid) {
        MoocUserT moocUserT = moocUserTMapper.selectById(uuid);
        return do2UserInfoModel(moocUserT);
    }

    @Override
    public UserInfoModel updateUserInfo(UserInfoModel userInfoModel) {
        // 将传入的数据z转换成MoocUserT
        MoocUserT moocUserT = new MoocUserT();

        moocUserT.setUuid(userInfoModel.getUuid());
        moocUserT.setNickName(userInfoModel.getNickname());
        moocUserT.setLifeState(Integer.parseInt(userInfoModel.getLifeState()));
        moocUserT.setBirthday(userInfoModel.getBirthday());
        moocUserT.setBiography(userInfoModel.getBiography());
        //moocUserT.setBeginTime(null);
        moocUserT.setHeadUrl(userInfoModel.getHeadAddress());
        moocUserT.setEmail(userInfoModel.getEmail());
        moocUserT.setAddress(userInfoModel.getAddress());
        moocUserT.setUserPhone(userInfoModel.getPhone());
        moocUserT.setUserSex(userInfoModel.getSex());
        //moocUserT.setUpdateTime(null);

        //存入数据库
        Integer result = moocUserTMapper.updateById(moocUserT);
        if (result > 0) {
            return getUserInfo(moocUserT.getUuid());
        }
        return userInfoModel;
    }
}
