package com.hs.blog.controller.user;

import com.hs.blog.pojo.dto.UserLoginDTO;
import com.hs.blog.pojo.dto.UserRegisterDTO;
import com.hs.blog.result.Result;
import com.hs.blog.service.IUserService;
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



}
