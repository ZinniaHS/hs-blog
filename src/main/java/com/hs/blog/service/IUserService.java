package com.hs.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hs.blog.pojo.dto.*;
import com.hs.blog.pojo.entity.Book;
import com.hs.blog.pojo.entity.User;
import com.hs.blog.pojo.vo.UserDetailVO;
import com.hs.blog.pojo.vo.UserInfoVO;
import com.hs.blog.pojo.vo.UserSubscribeBloggerVO;
import com.hs.blog.result.PageResult;
import com.hs.blog.result.Result;

import java.util.List;

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

    /**
     * 关注博主
     * @param bloggerId
     * @return
     */
    Result subscribeBlogger(Integer bloggerId);

    /**
     * 取关博主
     * @param bloggerId
     * @return
     */
    Result unsubscribeBlogger(Integer bloggerId);

    /**
     * 获取关注的博主列表
     * @param userId
     * @return
     */
    Result<List<UserSubscribeBloggerVO>> getSubscribedBlogger(Integer userId);

    /**
     * 管理端分页查询所有用户
     * @param userPageQueryDTO
     * @return
     */
    PageResult userPage(UserPageQueryDTO userPageQueryDTO);

    /**
     * 更新用户状态
     * @param lockStatus
     * @param id
     * @return
     */
    void updateUserLockStatus(Integer lockStatus, Integer id);

    /**
     * 获取用户信息
     * @param id
     * @return
     */
    Result<User> getUserById(Integer id);

    /**
     * 批量删除用户
     * @param ids
     * @return
     */
    void batchDeleteUser(List<Integer> ids);

    /**
     * 获取用户对该博主的关注状态
     * 进入博主信息页中触发判断，判断是否已经关注该博主
     * @param bloggerId 对象博主
     * @return Boolean true 已关注，false 未关注
     */
    Result getSubscribeStatus(Integer bloggerId);

    /**
     * 收藏图书
     * @param bookId
     * @return
     */
    Result collectBook(Integer bookId);

    /**
     * 取消收藏图书
     * @param bookId
     * @return
     */
    Result removeCollectBook(Integer bookId);

    /**
     * 查询图书是否已加入书架
     * @param bookId
     * @return
     */
    Result<Boolean> checkCollectBookStatus(Integer bookId);

    /**
     * 查询用户收藏的图书
     * @return
     */
    PageResult getCollectBooks(BookPageQueryDTO bookPageQueryDTO);
}
