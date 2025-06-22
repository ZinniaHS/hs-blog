package com.hs.blog.controller.admin;

import com.hs.blog.pojo.dto.BlogPageQueryDTO;
import com.hs.blog.pojo.entity.Blog;
import com.hs.blog.pojo.vo.BlogVO;
import com.hs.blog.result.PageResult;
import com.hs.blog.result.Result;
import com.hs.blog.service.IBlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "管理端博客接口",description = "博客接口")
@RestController("adminBlogController")
@RequestMapping("/api/admin/blog")
public class BlogController {

    @Autowired
    private IBlogService blogService;

    /**
     * 管理端分页查询博客信息
     * @param blogPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @Operation(summary = "管理端分页查询博客信息")
    public Result<PageResult> queryBlogByPage(BlogPageQueryDTO blogPageQueryDTO) {
        PageResult result = blogService.adminQueryBlogByPage(blogPageQueryDTO);
        return Result.success(result);
    }

    /**
     * 更新博客锁定状态
     * @param lockStatus 当前博客锁定状态
     * @param id 博客id
     * @return
     */
    @PostMapping("/lockStatus/{lockStatus}")
    @Operation(summary = "更新博客状态")
    public Result updateBlogLockStatus(@PathVariable("lockStatus") Integer lockStatus,
                                   @RequestParam("id") Integer id) {
        blogService.updateBlogLockStatus(lockStatus,id);
        return Result.success();
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
     * 管理员端修改博客
     * @param blog
     * @return
     */
    @PutMapping
    @Operation(summary = "管理员端修改博客")
    public Result updateBlog(@RequestBody Blog blog) {
        blogService.updateBlogForAdmin(blog);
        return Result.success();
    }

    /**
     * 批量删除博客
     * @param ids 多个选中的博客id
     * @return
     */
    @DeleteMapping("/batchDelete")
    @Operation(summary = "批量删除博客")
    public Result batchDeleteBlog(@RequestParam("ids") List<Integer> ids) {
        blogService.batchDeleteBlog(ids);
        return Result.success();
    }

}
