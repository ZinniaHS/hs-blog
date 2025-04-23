package com.hs.blog.controller.user;

import com.hs.blog.pojo.dto.BookPageQueryDTO;
import com.hs.blog.result.PageResult;
import com.hs.blog.result.Result;
import com.hs.blog.service.IBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@Tag(name = "客户端书籍接口",description = "书籍相关接口")
@RestController("userBookController")
@RequestMapping("/user/book")
public class BookController {

    @Autowired
    private IBookService bookService;

    @GetMapping("/page")
    @Operation(summary = "分页查询书籍信息")
    public Result<PageResult> queryBookByPage(BookPageQueryDTO bookPageQueryDTO) {
        PageResult result = bookService.pageQuery(bookPageQueryDTO);
        return Result.success(result);
    }
}
