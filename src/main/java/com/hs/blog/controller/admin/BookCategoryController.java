package com.hs.blog.controller.admin;

import com.hs.blog.pojo.dto.BookCategoryPageQueryDTO;
import com.hs.blog.pojo.dto.SaveBookCategoryDTO;
import com.hs.blog.pojo.entity.Book;
import com.hs.blog.pojo.entity.BookCategory;
import com.hs.blog.pojo.vo.BookCategoryVO;
import com.hs.blog.result.PageResult;
import com.hs.blog.result.Result;
import com.hs.blog.service.IBookCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Tag(name = "后台书籍分类接口",description = "书籍分类接口")
@RestController("adminBookCategoryController")
@RequestMapping("/api/admin/bookCategory")
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

    /**
     * 分页查询图书类型信息
     * @param bookCategoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询图书类型信息")
    public Result<PageResult> queryBookByPage(BookCategoryPageQueryDTO bookCategoryPageQueryDTO) {
        PageResult result = bookCategoryService.pageQuery(bookCategoryPageQueryDTO);
        return Result.success(result);
    }

    /**
     * 新增图书类型
     * @param saveBookCategoryDTO
     * @return
     */
    @PostMapping
    @Operation(summary = "新增图书类型")
    public Result saveCategory(@RequestBody SaveBookCategoryDTO saveBookCategoryDTO) {
        bookCategoryService.saveCategory(saveBookCategoryDTO);
        return Result.success();
    }

    /**
     * 删除书籍分类
     * @param id，parentId
     * @return
     */
    @DeleteMapping("/delete")
    @Operation(summary = "删除书籍分类")
    public Result deleteBookCategory(@RequestParam("id") Integer id,
                                     @RequestParam(value = "parentId", required = false) Integer parentId) {
        Result result =bookCategoryService.deleteBookCategory(id,parentId);
        return result;
    }

    /**
     * 更新书籍分类信息（名称）
     * @param bookCategory
     * @return
     */
    @PutMapping
    @Operation(summary = "更新书籍分类信息（名称）")
    public Result editBookCategory(@RequestBody BookCategory bookCategory) {
        Result result = bookCategoryService.editBookCategory(bookCategory);
        return Result.success(result);
    }
}
