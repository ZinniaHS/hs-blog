package com.hs.blog.controller.user;

import com.hs.blog.pojo.dto.UserDetailDTO;
import com.hs.blog.pojo.dto.UserLoginDTO;
import com.hs.blog.pojo.dto.UserRegisterDTO;
import com.hs.blog.pojo.entity.Blog;
import com.hs.blog.pojo.vo.UserDetailVO;
import com.hs.blog.pojo.vo.UserInfoVO;
import com.hs.blog.result.Result;
import com.hs.blog.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "客户端用户接口",description = "用户相关接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 用户登录接口
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody UserLoginDTO userLoginDTO) {
        return userService.login(userLoginDTO);
    }

    /**
     * 用户注册接口
     * @param userRegisterDTO
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody UserRegisterDTO userRegisterDTO) {
        return userService.register(userRegisterDTO);
    }

    /**
     * 用户登出接口
     * @param token
     * @return
     */
    @PostMapping("/logout")
    public Result logout(@RequestHeader("Authorization") String token) {
        return userService.logout(token);
    }

    /**
     * 发送邮箱注册验证码
     * @param email
     * @return
     */
    @PostMapping("/sendCaptcha")
    public Result sendCaptcha(@RequestParam("email") String email) {
        return userService.sendCaptcha(email);
    }

    /**
     * 验证邮箱是否存在
     * @param email
     * @return
     */
    @PostMapping("/verifyEmail")
    public Result verifyEmail(@RequestParam("email") String email) {
        return userService.verifyEmail(email);
    }

    /**
     * 验证是否是自己的页面
     * @param id 为-1时，代表是自己的页面，其他的需要判断
     * @return 返回状态码来判断
     *         如果code是1，则是自己的页面，data是用户id
     *         如果code是0，则不是自己的页面，msg是用户id
     */
    @PostMapping("/verifyIfIsMyself")
    public Result<UserInfoVO> verifyIfIsMyself(@RequestParam("id") Integer id) {
        return userService.verifyIfIsMyself(id);
    }

    /**
     * 获取用户信息
     * @param id
     * @return 返回状态码来判断
     *         如果code是1，则是自己的页面，data是用户id
     *         如果code是0，则不是自己的页面，msg是用户id
     */
    @GetMapping("/getUserInfoById")
    public Result<UserInfoVO> getUserInfoById(@RequestParam("id") Integer id) {
        return userService.getUserInfoById(id);
    }

    /**
     * 在资料编辑页面获取用户信息
     * @param id
     * @return
     */
    @GetMapping("/getUserDetail")
    public Result<UserDetailVO> getUserDetail(@RequestParam("id") Integer id) {
        return userService.getUserDetail(id);
    }

    /**
     * 修改用户个人信息
     * @param userDetailDTO
     * @return
     */
    @PutMapping
    @Operation(summary = "修改用户个人信息")
    public Result updateUserDetail(@RequestBody UserDetailDTO userDetailDTO) {
        userService.updateUserDetail(userDetailDTO);
        return Result.success();
    }

}
