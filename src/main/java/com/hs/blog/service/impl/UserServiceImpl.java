package com.hs.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hs.blog.constant.MessageConstant;
import com.hs.blog.mapper.UserMapper;
import com.hs.blog.pojo.dto.UserLoginDTO;
import com.hs.blog.pojo.dto.UserRegisterDTO;
import com.hs.blog.pojo.entity.User;
import com.hs.blog.result.Result;
import com.hs.blog.service.IUserService;
import com.hs.blog.utils.CaptchaUtil;
import com.hs.blog.utils.JWTUtil;
import com.hs.blog.utils.MailUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements IUserService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private MailUtil mailUtil;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 用户登录接口
     * @param userLoginDTO
     * @return
     */
    @Override
    public Result login(UserLoginDTO userLoginDTO) {
        String email = userLoginDTO.getEmail();
        String password = userLoginDTO.getPassword();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("email", email);
        User user = this.getOne(wrapper);
        // 用户不存在
        if(user==null)
            return Result.error(MessageConstant.ACCOUNT_NOT_FOUND);
        if(!passwordEncoder.matches(password,user.getPassword()))
            return Result.error(MessageConstant.PASSWORD_ERROR);
        // 生成token
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        String token = jwtUtil.createJWT(claims);
        return Result.success(token);
    }


    /**
     * 用户注册接口
     * @param userRegisterDTO
     * @return
     */
    @Override
    public Result register(UserRegisterDTO userRegisterDTO) {
        User user = new User();
        BeanUtils.copyProperties(userRegisterDTO, user);
        // 再次判断邮箱是否存在，状态码0表示已存在
        if(verifyEmail(userRegisterDTO.getEmail()).getCode()==0)
            return Result.error(MessageConstant.ALREADY_EXIST);
        // 判断验证码
        // 1.从redis中获取验证码
        // 2.将获取的验证码与用户输入的验证码进行比对
        String captchaInRedis = redisTemplate.opsForValue().get(user.getEmail());
        if (captchaInRedis == null || !captchaInRedis.equals(userRegisterDTO.getCaptcha()))
            return Result.error(MessageConstant.CAPTCHA_ERROR);
        // 如果验证码正确，则加密密码，将用户信息保存
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.save(user);
        return Result.success(MessageConstant.REGISTER_SUCCESS);
    }

    /**
     * 发送邮箱注册验证码
     * @param email
     * @return
     */
    @Override
    public Result sendCaptcha(String email) {
        // 1.生成验证码
        // 2.将验证码存入redis
        // 3.发送带有验证码的邮件到指定注册邮箱
        // 生成6位数字验证码
        String captcha = CaptchaUtil.generateCaptcha();
        System.out.println(captcha);
        // 存入redis，key为邮箱，value为6位验证码，设置过期时间5分钟
        redisTemplate.opsForValue()
                .set(email,captcha,5, TimeUnit.MINUTES);
        // 发送带有验证码的邮件到指定注册邮箱
        try {
            mailUtil.sendCaptchaEMail(email,captcha);
        }catch (Exception e){
            return Result.error(MessageConstant.CAPTCHA_SENT_FAIL);
        }
        return Result.success(MessageConstant.CAPTCHA_SENT);
    }

    /**
     * 验证邮箱是否存在
     * @param email
     * @return
     */
    @Override
    public Result verifyEmail(String email) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, email.trim());
        User user = this.getOne(wrapper);
        if (user != null)
            return Result.error(MessageConstant.ALREADY_EXIST);
        return Result.success(MessageConstant.EMAIL_OK);
    }

    /**
     * 用户登出接口
     * @param token
     * @return
     */
    @Override
    public Result logout(String token) {
        if (token != null && token.startsWith("Bearer "))
            token = token.replace("Bearer ", "");
        return jwtUtil.invalidateJWT(token);
    }


}
