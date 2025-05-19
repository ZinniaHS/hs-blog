package com.hs.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hs.blog.pojo.dto.UserDetailDTO;
import com.hs.blog.pojo.dto.UserLoginDTO;
import com.hs.blog.pojo.dto.UserRegisterDTO;
import com.hs.blog.pojo.entity.User;
import com.hs.blog.pojo.vo.UserDetailVO;
import com.hs.blog.pojo.vo.UserInfoVO;
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

    /**
     * 验证是否是自己的页面
     * @param id 为-1时，代表是自己的页面，其他的需要判断
     * @return 返回状态码来判断
     *         如果code是1，则是自己的页面，data是用户id
     *         如果code是0，则不是自己的页面，msg是用户id
     */
    Result verifyIfIsMyself(Integer id);

    /**
     * 获取用户信息
     * @param id
     * @return 返回状态码来判断
     *         如果code是1，则是自己的页面，data是用户id
     *         如果code是0，则不是自己的页面，msg是用户id
     */
    Result<UserInfoVO> getUserInfoById(Integer id);

    /**
     * 在资料编辑页面获取用户信息
     * @param id
     * @return
     */
    Result<UserDetailVO> getUserDetail(Integer id);

    /**
     * 修改用户个人信息
     * @param userDetailDTO
     * @return
     */
    void updateUserDetail(UserDetailDTO userDetailDTO);
}
