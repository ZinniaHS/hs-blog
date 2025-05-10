package com.hs.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hs.blog.pojo.dto.BlogDTO;
import com.hs.blog.pojo.entity.Blog;
import com.hs.blog.result.Result;

public interface IBlogService extends IService<Blog> {

    /**
     * 新增博客
     * @param blogDTO
     * @return
     */
    Result saveBlog(BlogDTO blogDTO);
}
