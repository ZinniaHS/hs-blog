package com.hs.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hs.blog.pojo.dto.BlogPageQueryDTO;
import com.hs.blog.pojo.dto.BlogPageQueryForOneDTO;
import com.hs.blog.pojo.dto.BlogPageQueryForOtherDTO;
import com.hs.blog.pojo.entity.Blog;
import com.hs.blog.pojo.vo.BlogLikeStarAndFollowVO;
import com.hs.blog.pojo.vo.BlogPageQueryVO;
import com.hs.blog.pojo.vo.BlogVO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface BlogMapper extends BaseMapper<Blog> {

    /**
     * 客户端分页查询博客信息
     * @param blogPageQueryDTO
     * @return
     */
    IPage<BlogPageQueryVO> queryBlogByPage(Page<BlogPageQueryVO> page, BlogPageQueryDTO blogPageQueryDTO);

    /**
     * 根据用户id分页查询他的所有博客
     * @param blogPageQueryForOneDTO
     * @return
     */
    IPage<BlogPageQueryVO> queryAllBlogsByUserId(Page<BlogPageQueryVO> page, BlogPageQueryForOneDTO blogPageQueryForOneDTO);

    /**
     * 管理端分页查询博客信息
     * @param blogPageQueryDTO
     * @return
     */
    IPage<BlogPageQueryVO> adminQueryBlogByPage(Page<BlogPageQueryVO> page, BlogPageQueryDTO blogPageQueryDTO);

    @Update("UPDATE `hs-blog`.blog SET view_count = view_count + 1 WHERE id = #{blogId}")
    void incrementViewCount(Integer blogId);

    /**
     * 根据用户id，获取该用户所有关注者的博客
     * @return
     */
    IPage<BlogPageQueryVO> getSubscription(Page<BlogPageQueryVO> page, BlogPageQueryForOtherDTO blogPageQueryForOtherDTO);

    /**
     * 博客点赞数量+1
     * @param userId,blogId
     * @return
     */
    @Insert("INSERT INTO `hs-blog`.user_blog_like (user_id, blog_id) " +
            "VALUES (#{userId}, #{blogId})")
    void incrementLikeCount(Integer userId, Integer blogId);

    /**
     * 博客收藏数量+1
     * @param blogId
     * @return
     */
    @Insert("INSERT INTO `hs-blog`.user_blog_star (user_id, blog_id) " +
            "VALUES (#{userId}, #{blogId})")
    void incrementStarCount(int userId, Integer blogId);

    /**
     * 当前用户对博客点赞数量-1
     * @param blogId
     * @return
     */
    @Delete("DELETE FROM `hs-blog`.user_blog_like WHERE user_id = #{userId} AND blog_id = #{blogId}")
    void decrementLikeCount(int userId, Integer blogId);

    /**
     * 当前用户对博客收藏数量-1
     * @param blogId
     * @return
     */
    @Delete("DELETE FROM `hs-blog`.user_blog_star WHERE user_id = #{userId} AND blog_id = #{blogId}")
    void decrementStarCount(int userId, Integer blogId);

    /**
     * 查询用户对博客的点赞、收藏和关注状态
     * @param userId 用户ID
     * @param blogId 博客ID
     * @return BlogLikeAndStarVO 包含三种状态
     */
    @Select("""
    SELECT 
        (SELECT 1 FROM `hs-blog`.user_blog_like 
         WHERE user_id = #{userId} 
           AND blog_id = #{blogId} 
           AND is_deleted = 0) > 0 AS isLiked,
        (SELECT 1 FROM `hs-blog`.user_blog_star 
         WHERE user_id = #{userId} 
           AND blog_id = #{blogId} 
           AND is_deleted = 0) > 0 AS isStarred,
        (SELECT 1 FROM `hs-blog`.user_follower 
         WHERE user_id = #{bloggerId} 
           AND follower_id = #{userId}) > 0 AS isFollowed
    """)
    BlogLikeStarAndFollowVO getLikeStarAndFollowStatus(int userId, Integer blogId, Integer bloggerId);

    /**
     * 根据用户id，获取该用户收藏的博客
     * @return
     */
    IPage<BlogPageQueryVO> getStarBlogs(Page<BlogPageQueryVO> page, BlogPageQueryForOtherDTO blogPageQueryForOtherDTO);

    /**
     * 根据id查询博客信息
     * @param id
     * @return
     */
    BlogVO queryById(Integer id);

    /**
     * 统计该分类下的博客数量
     * @param id
     * @return
     */
    @Select("SELECT COUNT(*) FROM `hs-blog`.blog WHERE category_id = #{id}")
    int countByCategoryId(Integer id);
}
