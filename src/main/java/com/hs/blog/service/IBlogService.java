package com.hs.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hs.blog.pojo.dto.BlogDTO;
import com.hs.blog.pojo.dto.BlogPageQueryDTO;
import com.hs.blog.pojo.dto.BlogPageQueryForOneDTO;
import com.hs.blog.pojo.dto.BlogPageQueryForSubscribeDTO;
import com.hs.blog.pojo.entity.Blog;
import com.hs.blog.pojo.vo.BlogLikeAndStarVO;
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
    PageResult queryBlogByPage(BlogPageQueryDTO blogPageQueryDTO);

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

    /**
     * 管理端分页查询博客信息
     * @param blogPageQueryDTO
     * @return
     */
    PageResult adminQueryBlogByPage(BlogPageQueryDTO blogPageQueryDTO);

    /**
     * 更新博客锁定状态
     * @param lockStatus 当前博客锁定状态
     * @param id 博客id
     * @return
     */
    void updateBlogLockStatus(Integer lockStatus, Integer id);

    /**
     * 管理员端修改博客
     * @param blog
     * @return
     */
    void updateBlogForAdmin(Blog blog);

    /**
     * 批量删除博客
     * @param ids 多个选中的博客id
     * @return
     */
    void batchDeleteBlog(List<Integer> ids);

    /**
     * 博客浏览数量+1
     * @param id
     * @return
     */
    Result incrementViewCount(Integer id);

    /**
     * 获取浏览量排行前五的博客
     * @return
     */
    List<Blog> getTopFiveBlog();

    /**
     * 根据用户id，获取该用户所有关注者的博客
     * @return
     */
    PageResult getSubscription(BlogPageQueryForSubscribeDTO blogPageQueryForSubscribeDTO);

    /**
     * 博客点赞数量+1
     * @param blogId
     * @return
     */
    Result incrementLikeCount(Integer blogId);

    /**
     * 博客收藏数量+1
     * @param blogId
     * @return
     */
    Result incrementStarCount(Integer blogId);

    /**
     * 获取博客点赞和收藏的状态
     * @param blogId
     * @return
     */
    Result<BlogLikeAndStarVO> getStatusOfLikeAndStar(Integer blogId);
}
