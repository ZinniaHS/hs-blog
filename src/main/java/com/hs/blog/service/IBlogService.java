package com.hs.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hs.blog.pojo.dto.BlogDTO;
import com.hs.blog.pojo.dto.BlogPageQueryDTO;
import com.hs.blog.pojo.dto.BlogPageQueryForOneDTO;
import com.hs.blog.pojo.entity.Blog;
import com.hs.blog.pojo.vo.BlogVO;
import com.hs.blog.result.PageResult;
import com.hs.blog.result.Result;

import java.util.List;

public interface IBlogService extends IService<Blog> {

    /**
     * 新增博客
     * @param blogDTO
     * @return
     */
    Result saveBlog(BlogDTO blogDTO);

    /**
     * 客户端分页查询博客信息
     * @param blogPageQueryDTO
     * @return
     */
    PageResult pageQueryForUser(BlogPageQueryDTO blogPageQueryDTO);

    /**
     * 根据id查询博客信息
     * @param id
     * @return
     */
    BlogVO queryById(Integer id);

    /**
     * 根据用户id分页查询他的所有博客
     * @param blogPageQueryForOneDTO
     * @return
     */
    PageResult queryAllBlogsByUserId(BlogPageQueryForOneDTO blogPageQueryForOneDTO);

    /**
     * 新增或修改博客
     * @param blog
     * @return
     */
    Result updateBlog(Blog blog);

    /**
     * 删除博客
     * @param id
     * @return
     */
    Result deleteBlog(Integer id);
}
