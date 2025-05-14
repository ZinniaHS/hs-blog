package com.hs.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hs.blog.constant.MessageConstant;
import com.hs.blog.context.CustomUserDetails;
import com.hs.blog.mapper.BlogCategoryMapper;
import com.hs.blog.mapper.BlogMapper;
import com.hs.blog.mapper.BookCategoryMapper;
import com.hs.blog.mapper.UserMapper;
import com.hs.blog.pojo.dto.BlogDTO;
import com.hs.blog.pojo.dto.BlogPageQueryDTO;
import com.hs.blog.pojo.entity.*;
import com.hs.blog.pojo.vo.BlogPageQueryVO;
import com.hs.blog.pojo.vo.BlogVO;
import com.hs.blog.result.PageResult;
import com.hs.blog.result.Result;
import com.hs.blog.service.IBlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BlogServiceImpl
        extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private BlogCategoryMapper blogCategoryMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 新增博客
     * @param blogDTO
     * @return
     */
    @Override
    public Result saveBlog(BlogDTO blogDTO) {
        System.out.println("================"+blogDTO);
        String userId = null;
        // 获取当前登录用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        }
        if(userId == null)
            return Result.error(MessageConstant.NOT_LOGIN);
        Blog blog = new Blog();
        BeanUtils.copyProperties(blogDTO, blog);
        blog.setUserId(Integer.valueOf(userId));
        this.save(blog);
        return Result.success();
    }

    /**
     * 客户端分页查询博客信息
     * @param blogPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQueryForUser(BlogPageQueryDTO blogPageQueryDTO) {
        Page<BlogPageQueryVO> page = new Page<>();
        IPage<BlogPageQueryVO> res = blogMapper.pageQueryForUser(page, blogPageQueryDTO);
        System.out.println(res.getRecords());
        return new PageResult(page.getTotal(), page.getRecords());
    }

    /**
     * 根据id查询博客信息
     * @param id
     * @return
     */
    @Override
    public BlogVO queryById(Integer id) {
        BlogVO blogVO = new BlogVO();
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        Blog blog = blogMapper.selectOne(queryWrapper);
        BeanUtils.copyProperties(blog, blogVO);
        User user = userMapper.selectById(blog.getUserId());
        blogVO.setUserAvatar(user.getAvatarUrl());
        blogVO.setUsername(user.getUsername());
        BlogCategory blogCategory = blogCategoryMapper.selectById(blog.getCategoryId());
        blogVO.setCategoryName(blogCategory.getName());
        System.out.println(blogVO);
        return blogVO;
    }

    /**
     * 根据用户id分页查询他的所有博客
     * @param userId
     * @return
     */
    @Override
    public PageResult queryAllBlogsByUserId(Integer userId) {
        Page<BlogPageQueryVO> page = new Page<>();
        IPage<BlogPageQueryVO> res = blogMapper.queryAllBlogsByUserId(page, userId);
        return new PageResult(page.getTotal(), page.getRecords());
    }

}
