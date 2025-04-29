package com.hs.blog.controller.user;

import com.hs.blog.pojo.dto.UserLoginDTO;
import com.hs.blog.result.Result;
import com.hs.blog.service.IUserService;
import com.hs.blog.utils.CaptchaUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "客户端用户接口",description = "用户相关接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 用户登录接口
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody UserLoginDTO userLoginDTO) {

        return null;
    }

    /**
     * 发送邮箱注册验证码
     * @param mail
     * @return
     */
    @PostMapping("/captcha")
    public Result captcha(String mail) {
        System.out.println(CaptchaUtil.generateCaptcha());

        return null;
    }

}
