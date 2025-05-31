package com.hs.blog.controller.user;

import com.hs.blog.pojo.dto.*;
import com.hs.blog.pojo.entity.Blog;
import com.hs.blog.pojo.vo.BlogLikeStarAndFollowVO;
import com.hs.blog.pojo.vo.BlogVO;
import com.hs.blog.result.PageResult;
import com.hs.blog.result.Result;
import com.hs.blog.service.IBlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "客户端博客接口",description = "博客接口")
@RestController("userBlogController")
@RequestMapping("/user/blog")
public class BlogController {

    @Autowired
    private IBlogService blogService;

    /**
     * 新增博客
     * @param blogDTO
     * @return
     */
    @PostMapping
    @Operation(summary = "新增博客")
    public Result saveBlog(@RequestBody BlogDTO blogDTO) {
        return blogService.saveBlog(blogDTO);
    }

    /**
     * 客户端分页查询博客信息
     * @param blogPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @Operation(summary = "客户端分页查询博客信息")
    public Result<PageResult> queryBlogByPage(BlogPageQueryDTO blogPageQueryDTO) {
        PageResult result = blogService.queryBlogByPage(blogPageQueryDTO);
        return Result.success(result);
    }

    /**
     * 根据id查询博客信息
     * @param id
     * @return
     */
    @GetMapping("{id}")
    @Operation(summary = "根据id查询博客信息")
    public Result<BlogVO> queryBlogById(@PathVariable("id") Integer id) {
        BlogVO blogVO = blogService.queryById(id);
        return Result.success(blogVO);
    }

    /**
     * 根据用户id分页查询他的所有博客
     * @param blogPageQueryForOneDTO
     * @return
     */
    @GetMapping("/queryAllBlogsByUserId")
    @Operation(summary = "根据用户id分页查询他的所有博客")
    public Result<PageResult> queryAllBlogsByUserId(BlogPageQueryForOneDTO blogPageQueryForOneDTO) {
        PageResult res = blogService.queryAllBlogsByUserId(blogPageQueryForOneDTO);
        return Result.success(res);
    }

    /**
     * 新增或修改博客
     * @param blog
     * @return
     */
    @PutMapping
    @Operation(summary = "新增或修改博客")
    public Result updateBlog(@RequestBody Blog blog) {
        System.out.println("=================="+blog);
        blogService.updateBlog(blog);
        return Result.success();
    }

    /**
     * 删除博客
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除博客")
    public Result deleteBlog(@PathVariable("id") Integer id) {
        blogService.deleteBlog(id);
        return Result.success();
    }

    /**
     * 博客浏览数量+1
     * @param id
     * @return
     */
    @PostMapping("/incrementViewCount/{id}")
    @Operation(summary = "博客浏览数量+1")
    public Result incrementViewCount(@PathVariable("id") Integer id) {
        return blogService.incrementViewCount(id);
    }

    /**
     * 获取浏览量排行前五的博客
     * @return
     */
    @GetMapping("/getTopFiveBlog")
    @Operation(summary = "获取浏览量排行前五的博客")
    public Result<List<Blog>> getTopFiveBlog() {
        return Result.success(blogService.getTopFiveBlog());
    }

    /**
     * 获取个人浏览量排行前五的博客
     * @return
     */
    @GetMapping("/getTopFiveBlogForOne/{userId}")
    @Operation(summary = "获取个人浏览量排行前五的博客")
    public Result<List<Blog>> getTopFiveBlogForOne(@PathVariable("userId") Integer userId) {
        return Result.success(blogService.getTopFiveBlogForOne(userId));
    }


    /**
     * 根据用户id，获取该用户所有关注者的博客
     * @return
     */
    @GetMapping("/getSubscription")
    @Operation(summary = "根据用户id，获取该用户所有关注者的博客")
    public Result<PageResult> getSubscription(BlogPageQueryForOtherDTO blogPageQueryForOtherDTO) {
        return Result.success(blogService.getSubscription(blogPageQueryForOtherDTO));
    }

    /**
     * 根据用户id，获取该用户收藏的博客
     * @return
     */
    @GetMapping("/getStarBlogs")
    @Operation(summary = "根据用户id，获取该用户收藏的博客")
    public Result<PageResult> getStarBlogs(BlogPageQueryForOtherDTO blogPageQueryForOtherDTO) {
        return Result.success(blogService.getStarBlogs(blogPageQueryForOtherDTO));
    }

    /**
     * 当前用户对博客点赞数量+1
     * @param blogId
     * @return
     */
    @PostMapping("/incrementLikeCount/{blogId}")
    @Operation(summary = "当前用户对博客点赞数量+1")
    public Result incrementLikeCount(@PathVariable("blogId") Integer blogId) {
        return blogService.incrementLikeCount(blogId);
    }

    /**
     * 当前用户对博客点赞数量-1
     * @param blogId
     * @return
     */
    @PostMapping("/decrementLikeCount/{blogId}")
    @Operation(summary = "当前用户对博客点赞数量-1")
    public Result decrementLikeCount(@PathVariable("blogId") Integer blogId) {
        return blogService.decrementLikeCount(blogId);
    }

    /**
     * 当前用户对博客收藏数量+1
     * @param blogId
     * @return
     */
    @PostMapping("/incrementStarCount/{blogId}")
    @Operation(summary = "当前用户对博客收藏数量+1")
    public Result incrementStarCount(@PathVariable("blogId") Integer blogId) {
        return blogService.incrementStarCount(blogId);
    }

    /**
     * 当前用户对博客收藏数量-1
     * @param blogId
     * @return
     */
    @PostMapping("/decrementStarCount/{blogId}")
    @Operation(summary = "当前用户对博客收藏数量+1")
    public Result decrementStarCount(@PathVariable("blogId") Integer blogId) {
        return blogService.decrementStarCount(blogId);
    }

    /**
     * 获取博客点赞和收藏的状态
     * @param blogId
     * @return
     */
    @GetMapping("/getLikeStarAndFollowStatus")
    @Operation(summary = "获取博客点赞和收藏的状态")
    public Result<BlogLikeStarAndFollowVO> getLikeStarAndFollowStatus(
       @RequestParam Integer blogId,
       @RequestParam Integer bloggerId
    ) {
        return blogService.getLikeStarAndFollowStatus(blogId,bloggerId);
    }

}
