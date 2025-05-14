package com.hs.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hs.blog.pojo.dto.BlogPageQueryDTO;
import com.hs.blog.pojo.entity.Blog;
import com.hs.blog.pojo.vo.BlogPageQueryVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogMapper extends BaseMapper<Blog> {

    /**
     * 客户端分页查询博客信息
     * @param blogPageQueryDTO
     * @return
     */
    IPage<BlogPageQueryVO> pageQueryForUser(Page<BlogPageQueryVO> page, BlogPageQueryDTO blogPageQueryDTO);

    /**
     * 根据用户id分页查询他的所有博客
     * @param userId
     * @return
     */
    IPage<BlogPageQueryVO> queryAllBlogsByUserId(Page<BlogPageQueryVO> page, Integer userId);
}
