package com.hs.blog.controller.admin;

import com.hs.blog.pojo.dto.SaveBlogCategoryDTO;
import com.hs.blog.pojo.entity.BlogCategory;
import com.hs.blog.pojo.vo.BlogCategoryVO;
import com.hs.blog.result.Result;
import com.hs.blog.service.IBlogCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "后台博客分类接口",description = "博客分类接口")
@RestController("adminBlogCategoryController")
@RequestMapping("/api/admin/blogCategory")
public class BlogCategoryController {

    @Autowired
    private IBlogCategoryService blogCategoryService;

    /**
     * 查询所有的博客分类
     * @return
     */
    @GetMapping("/all")
    @Operation(summary = "查询所有的博客分类")
    public Result<List<BlogCategoryVO>> getBlogCategory() {
        List<BlogCategoryVO> list = blogCategoryService.getBlogCategory();
        return Result.success(list);
    }

    /**
     * 新增博客类型
     * @param saveBlogCategoryDTO
     * @return
     */
    @PostMapping
    @Operation(summary = "新增博客类型")
    public Result saveCategory(@RequestBody SaveBlogCategoryDTO saveBlogCategoryDTO) {
        return blogCategoryService.saveCategory(saveBlogCategoryDTO);
    }

    /**
     * 删除博客分类
     * @param id
     * @param parentId
     * @return
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除博客分类")
    public Result deleteBlogCategory(@RequestParam("id") Integer id,
                                     @RequestParam(value = "parentId", required = false) Integer parentId) {
        return blogCategoryService.deleteBlogCategory(id,parentId);
    }

    /**
     * 更新博客分类信息（名称）
     * @param blogCategory
     * @return
     */
    @PutMapping
    @Operation(summary = "更新博客分类信息（名称）")
    public Result editBlogCategory(@RequestBody BlogCategory blogCategory) {
        Result result = blogCategoryService.editBlogCategory(blogCategory);
        return Result.success(result);
    }
}
