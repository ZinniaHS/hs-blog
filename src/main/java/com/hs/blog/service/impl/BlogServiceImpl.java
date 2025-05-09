package com.hs.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hs.blog.mapper.BlogMapper;
import com.hs.blog.pojo.entity.Blog;
import com.hs.blog.service.IBlogService;

public class BlogServiceImpl
        extends ServiceImpl<BlogMapper, Blog> implements IBlogService {
}
