package com.hs.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hs.blog.mapper.BlogMapper;
import com.hs.blog.pojo.dto.BlogDTO;
import com.hs.blog.pojo.entity.Blog;
import com.hs.blog.result.Result;
import com.hs.blog.service.IBlogService;
import org.springframework.stereotype.Service;

@Service
public class BlogServiceImpl
        extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

    /**
     * 新增博客
     * @param blogDTO
     * @return
     */
    @Override
    public Result saveBlog(BlogDTO blogDTO) {
        return null;
    }
}
