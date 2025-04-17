package com.hs.blog.controller.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@Tag(name = "后台书籍分类接口",description = "书籍分类接口")
@RestController
@RequestMapping("/admin/bookCategory")
public class BookCategoryController {


}
