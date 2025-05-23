package com.hs.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hs.blog.constant.MessageConstant;
import com.hs.blog.context.CustomUserDetails;
import com.hs.blog.mapper.BlogMapper;
import com.hs.blog.mapper.UserMapper;
import com.hs.blog.pojo.dto.UserDetailDTO;
import com.hs.blog.pojo.dto.UserLoginDTO;
import com.hs.blog.pojo.dto.UserRegisterDTO;
import com.hs.blog.pojo.entity.Blog;
import com.hs.blog.pojo.entity.BlogCategory;
import com.hs.blog.pojo.entity.User;
import com.hs.blog.pojo.vo.UserDetailVO;
import com.hs.blog.pojo.vo.UserInfoVO;
import com.hs.blog.result.Result;
import com.hs.blog.service.IUserService;
import com.hs.blog.utils.CaptchaUtil;
import com.hs.blog.utils.JWTUtil;
import com.hs.blog.utils.MailUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BlogMapper blogMapper;

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
        // 将返回map，可以选择把token，userId，username，avatarUrl封装到map中
        HashMap<String, Object> map = new HashMap<>();
        // 生成token，并放入结果map中
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        String token = jwtUtil.createJWT(claims);
        // 放入其他用户信息
        map.put("token", token);
        map.put("userId", user.getId());
        map.put("username", user.getUsername());
        map.put("avatarUrl", user.getAvatarUrl());
        return Result.success(map);
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

    /**
     * 验证是否是自己的页面
     * @param id 为-1时，代表是自己的页面，其他的需要判断
     * @return 返回状态码来判断
     *         如果code是1，则是自己的页面，data是用户id
     *         如果code是0，则不是自己的页面，msg是用户id
     */
    @Override
    public Result verifyIfIsMyself(Integer id) {
        String userId = null;
        // 获取当前登录用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        }
        // 如果id为-1，则代表是自己的页面
        if(id == -1)
            return Result.success(userId);
        // 除此之外就是别人的页面，返回用户id
        return Result.error(String.valueOf(id));
    }

    /**
     * 获取用户信息
     * @param id
     * @return 返回状态码来判断
     *         如果code是1，则是自己的页面，data是用户id
     *         如果code是0，则不是自己的页面，msg是用户id
     */
    @Override
    public Result<UserInfoVO> getUserInfoById(Integer id) {
        String userId = null;
        // 获取当前登录用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        }else{
            // 如果没有获取到用户信息，则代表是游客
            userId = "-2";
        }
        UserInfoVO userInfoVO;
        // 如果id为-1，则代表是自己的页面
        if(id == -1 || Integer.valueOf(userId).equals(id)){
            userInfoVO = userMapper.getUserInfoById(Integer.valueOf(userId));
            userInfoVO.setMyPage(true);
        }else{
            // 除此之外就是别人的页面，返回用户id
            userInfoVO = userMapper.getUserInfoById(Integer.valueOf(id));
            userInfoVO.setMyPage(false);
        }
        System.out.println("==================="+userInfoVO);
        return Result.success(userInfoVO);
//        // 查询用户博客数量
//        userInfoVO.setTotalBlogs(Math
//                .toIntExact(
//                blogMapper.selectCount(
//                new QueryWrapper<Blog>()
//                .eq("user_id", userInfoVO.getId()))));
//        // 查询总浏览数
//        userInfoVO.setTotalViews((int) blogMapper.selectList(
//                        new QueryWrapper<Blog>()
//                                .eq("user_id", userInfoVO.getId()))
//                .stream()
//                .mapToLong(Blog::getViewCount)
//                .sum());
//        // 查询总点赞数
//        userInfoVO.setTotalLikes((int) blogMapper.selectList(
//                new QueryWrapper<Blog>()
//                .eq("user_id", userInfoVO.getId()))
//                .stream()
//                .mapToLong(Blog::getLikeCount)
//                .sum());
//        // 查询总收藏数
//        userInfoVO.setTotalStars((int) blogMapper.selectList(
//                        new QueryWrapper<Blog>()
//                                .eq("user_id", userInfoVO.getId()))
//                .stream()
//                .mapToLong(Blog::getStarCount)
//                .sum());
    }

    /**
     * 在资料编辑页面获取用户信息
     * @param id
     * @return
     */
    @Override
    public Result<UserDetailVO> getUserDetail(Integer id) {
        UserDetailVO userDetailVO = new UserDetailVO();
        User user = this.getById(id);
        BeanUtils.copyProperties(user, userDetailVO);
        return Result.success(userDetailVO);
    }

    /**
     * 修改用户个人信息
     * @param userDetailDTO
     * @return
     */
    @Override
    public void updateUserDetail(UserDetailDTO userDetailDTO) {
        // 先从数据库获取原始用户数据
        User user = this.getById(userDetailDTO.getId());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setUsername(userDetailDTO.getUsername());
        user.setAvatarUrl(userDetailDTO.getAvatarUrl());
        user.setDescription(userDetailDTO.getDescription());
        user.setPhone(userDetailDTO.getPhone());
        this.updateById(user);
    }

    /**
     * 关注博主
     * @param bloggerId
     * @return
     */
    @Override
    public Result subscribeBlogger(Integer bloggerId) {
        // 当前登录用户是粉丝follower
        Integer followerId = getUserId();
        userMapper.subscribeBlogger(bloggerId,followerId);
        return Result.success();
    }

    /**
     * 取关博主
     * @param bloggerId
     * @return
     */
    @Override
    public Result unsubscribeBlogger(Integer bloggerId) {
        // 当前登录用户是粉丝follower
        Integer followerId = getUserId();
        userMapper.unsubscribeBlogger(bloggerId,followerId);
        return Result.success();
    }

    /**
     * 获取当前用户登录id
     * @return 返回用户id，不存在则返回-1
     */
    public synchronized Integer getUserId() {
        String userId = null;
        // 获取当前登录用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        }
        return userId != null ? Integer.valueOf(userId) : -1;
    }
}
