package com.hs.blog.controller.admin;

import com.hs.blog.pojo.dto.BookPageQueryDTO;
import com.hs.blog.pojo.entity.Book;
import com.hs.blog.pojo.entity.User;
import com.hs.blog.pojo.vo.BookVO;
import com.hs.blog.result.PageResult;
import com.hs.blog.result.Result;
import com.hs.blog.service.IBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Tag(name = "后台书籍管理接口",description = "书籍相关接口")
@RestController
@RequestMapping("/admin/book")
public class BookController  {

    @Autowired
    private IBookService bookService;

    /**
     * 根据id查询书籍信息
     * @param id
     * @return 返回BookVO对象
     */
    @GetMapping("{id}")
    @Operation(summary = "根据id查询书籍信息")
    public Result<BookVO> queryBookById(@PathVariable("id") Integer id) {
        BookVO bookVO = bookService.queryById(id);
        return Result.success(bookVO);
    }

    /**
     * 分页查询书籍
     * @param bookPageQueryDTO
     * 除了page，limit外，其余字段进行模糊查询
     * @return
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询书籍信息")
    public Result<PageResult> queryBookByPage(BookPageQueryDTO bookPageQueryDTO) {
        PageResult result = bookService.pageQuery(bookPageQueryDTO);
        return Result.success(result);
    }


    /**
     * 更新书籍状态
     * @param status 当前书籍状态
     * @param id 书籍id
     * @return
     */
    @PostMapping("/status/{status}")
    @Operation(summary = "更新书籍状态")
    public Result updateBookStatus(@PathVariable("status") Integer status,
                                   @RequestParam("id") Long id) {
        System.out.println("status:"+status+" id:"+id);
        bookService.updateBookStatus(status,id);
        return Result.success();
    }

    /**
     * 新增书籍信息
     * @param book
     * @return
     */
    @PostMapping
    @Operation(summary = "新增书籍信息")
    public Result<Book> saveBook(@RequestBody Book book) {
        bookService.saveBook(book);
        return Result.success();
    }

    /**
     * 更新书籍信息
     * @param book
     * @return
     */
    @PutMapping
    @Operation(summary = "更新书籍信息")
    public Result updateBook(@RequestBody Book book) {
        bookService.updateBook(book);
        return Result.success();
    }

    /**
     * 批量删除书籍信息
     * @param ids 多个选中的图书id
     * @return
     */
    @DeleteMapping("/batchDelete")
    @Operation(summary = "批量删除书籍信息")
    public Result batchDeleteBook(@RequestParam("ids") List<Integer> ids) {
        bookService.batchDeleteBook(ids);
        return Result.success();
    }

}
