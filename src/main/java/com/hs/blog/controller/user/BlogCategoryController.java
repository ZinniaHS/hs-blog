package com.hs.blog.controller.user;

import com.hs.blog.pojo.vo.BlogCategoryVO;
import com.hs.blog.result.Result;
import com.hs.blog.service.IBlogCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "客户端博客分类接口",description = "博客分类接口")
@RestController("userBlogCategoryController")
@RequestMapping("/user/blogCategory")
public class BlogCategoryController {

    @Autowired
    private IBlogCategoryService blogCategoryService;

    @GetMapping("/all")
    @Operation(summary = "查询所有的博客分类")
    public Result<List<BlogCategoryVO>> getBlogCategory() {
        List<BlogCategoryVO> list = blogCategoryService.getBlogCategory();
        System.out.println(list);
        return Result.success(list);
    }

}
