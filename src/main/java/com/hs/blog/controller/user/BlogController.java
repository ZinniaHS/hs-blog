package com.hs.blog.controller.user;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "客户端博客接口",description = "博客接口")
@RestController("userBlogController")
@RequestMapping("/user/blog")
public class BlogController {



}
