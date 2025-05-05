package com.hs.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hs.blog.pojo.dto.UserLoginDTO;
import com.hs.blog.pojo.dto.UserRegisterDTO;
import com.hs.blog.pojo.entity.User;
import com.hs.blog.result.Result;

public interface IUserService extends IService<User> {

    /**
     * 用户注册接口
     * @param userRegisterDTO
     * @return
     */
    Result register(UserRegisterDTO userRegisterDTO);

    /**
     * 发送邮箱注册验证码
     * @param email
     * @return
     */
    Result sendCaptcha(String email);

    /**
     * 用户登录接口
     * @param userLoginDTO
     * @return
     */
    Result login(UserLoginDTO userLoginDTO);

    /**
     * 验证邮箱
     * @param email
     * @return
     */
    Result verifyEmail(String email);

    /**
     * 用户登出接口
     * @param token
     * @return
     */
    Result logout(String token);
}
