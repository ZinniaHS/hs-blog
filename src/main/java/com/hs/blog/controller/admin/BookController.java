package com.hs.blog.controller.admin;

import com.hs.blog.pojo.entity.Book;
import com.hs.blog.result.Result;
import com.hs.blog.service.IBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "后台书籍管理接口")
@RestController
@RequestMapping("/admin/book")
public class BookController  {

    @Autowired
    private IBookService bookService;

    @GetMapping("{id}")
    @ApiOperation("根据id查询书籍信息")
    public Result<Book> queryBookById(@PathVariable("id") Integer id) {
        return Result.success(bookService.getById(id)) ;
    }

}
