package com.hs.blog.controller.user;

import com.hs.blog.pojo.dto.BlogDTO;
import com.hs.blog.pojo.dto.BlogPageQueryDTO;
import com.hs.blog.pojo.dto.BlogPageQueryForOneDTO;
import com.hs.blog.pojo.dto.BookPageQueryDTO;
import com.hs.blog.pojo.entity.Blog;
import com.hs.blog.pojo.entity.Book;
import com.hs.blog.pojo.vo.BlogVO;
import com.hs.blog.pojo.vo.BookVO;
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
        System.out.println("==========="+blogPageQueryForOneDTO);
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
        blogService.updateBlog(blog);
        return Result.success();
    }

    /**
     * 删除博客
     * @param id
     * @return
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除博客")
    public Result batchDeleteBook(@RequestParam("id") Integer id) {
        blogService.deleteBlog(id);
        return Result.success();
    }
}
