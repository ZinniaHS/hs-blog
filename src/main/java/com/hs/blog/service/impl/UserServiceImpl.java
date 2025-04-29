package com.hs.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hs.blog.mapper.UserMapper;
import com.hs.blog.pojo.entity.User;
import com.hs.blog.service.IUserService;

public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements IUserService {
}
