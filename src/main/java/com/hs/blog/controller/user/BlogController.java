package com.hs.blog.controller.user;

import com.hs.blog.pojo.dto.BlogDTO;
import com.hs.blog.result.Result;
import com.hs.blog.service.IBlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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



}
