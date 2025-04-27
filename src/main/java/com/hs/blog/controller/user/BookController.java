package com.hs.blog.controller.user;

import com.hs.blog.pojo.dto.BookPageQueryDTO;
import com.hs.blog.pojo.dto.SelectedCategoryBooksDTO;
import com.hs.blog.pojo.vo.BookVO;
import com.hs.blog.result.PageResult;
import com.hs.blog.result.Result;
import com.hs.blog.service.IBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Tag(name = "客户端书籍接口",description = "书籍相关接口")
@RestController("userBookController")
@RequestMapping("/user/book")
public class BookController {

    @Autowired
    private IBookService bookService;

    /**
     * 客户端分页查询书籍
     * @param bookPageQueryDTO
     * 大部分同管理端一致但要筛选已上架状态书籍
     * @return
     */
    @GetMapping("/page")
    @Operation(summary = "客户端分页查询书籍信息")
    public Result<PageResult> queryBookByPage(BookPageQueryDTO bookPageQueryDTO) {
        PageResult result = bookService.pageQueryForUser(bookPageQueryDTO);
        return Result.success(result);
    }

    /**
     * 根据一级，二级分类id分页查询图书
     * @return PageResult：总记录数和图书数据
     */
    @GetMapping("/selectedCategoryBooks")
    @Operation(summary = "根据一级，二级分类id分页查询图书")
    public Result<PageResult> getSelectedCategory(SelectedCategoryBooksDTO selectedCategoryBooksDTO) {
        PageResult result = bookService.getSelectedCategoryBooks(selectedCategoryBooksDTO);
        return Result.success(result);
    }

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

}
