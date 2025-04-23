package com.hs.blog.controller.user;

import com.hs.blog.pojo.vo.BookCategoryVO;
import com.hs.blog.result.Result;
import com.hs.blog.service.IBookCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@Tag(name = "客户端书籍分类接口",description = "书籍分类接口")
@RestController("userBookCategoryController")
@RequestMapping("/user/bookCategory")
public class BookCategoryController {

    @Autowired
    private IBookCategoryService bookCategoryService;

    /**
     * 查询所有的图书分类
     * @return BookCategoryVO
     */
    @GetMapping("/all")
    @Operation(summary = "查询所有的图书分类")
    public Result<List<BookCategoryVO>> getBookCategory() {
        List<BookCategoryVO> list = bookCategoryService.getBookCategory();
        return Result.success(list);
    }
}
