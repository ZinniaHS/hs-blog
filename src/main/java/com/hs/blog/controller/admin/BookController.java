package com.hs.blog.controller.admin;

import com.hs.blog.pojo.dto.BookPageQueryDTO;
import com.hs.blog.pojo.entity.Book;
import com.hs.blog.pojo.entity.User;
import com.hs.blog.result.PageResult;
import com.hs.blog.result.Result;
import com.hs.blog.service.IBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "后台书籍管理接口",description = "书籍相关接口")
@RestController
@RequestMapping("/admin/book")
public class BookController  {

    @Autowired
    private IBookService bookService;

    @GetMapping("{id}")
    @Operation(summary = "根据id查询书籍信息",description = "")
    public Result<Book> queryBookById(@PathVariable("id") Integer id) {
        return Result.success(bookService.getById(id)) ;
    }

    /**
     * 分页查询书籍
     * @param bookPageQueryDTO
     * 除了page，limit外，其余字段进行模糊查询
     * @return
     */
    @CrossOrigin
    @GetMapping("/page")
    @Operation(summary = "分页查询书籍信息")
    public Result<PageResult> queryBookByPage(BookPageQueryDTO bookPageQueryDTO) {
        PageResult result = bookService.pageQuery(bookPageQueryDTO);
        return Result.success(result);
    }

    @PostMapping
    @Operation(summary = "新增书籍信息")
    public Result<Book> addBook(@RequestBody Book book) {
        bookService.save(book);
        return Result.success();
    }
}
