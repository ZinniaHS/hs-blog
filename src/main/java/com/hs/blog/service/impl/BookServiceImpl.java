package com.hs.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hs.blog.constant.StatusConstant;
import com.hs.blog.mapper.BookCategoryMapper;
import com.hs.blog.mapper.BookMapper;
import com.hs.blog.pojo.dto.BookPageQueryDTO;
import com.hs.blog.pojo.entity.Book;
import com.hs.blog.pojo.entity.BookCategory;
import com.hs.blog.pojo.vo.BookVO;
import com.hs.blog.result.PageResult;
import com.hs.blog.result.Result;
import com.hs.blog.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl
        extends ServiceImpl<BookMapper, Book> implements IBookService {

    @Autowired
    private BookCategoryMapper bookCategoryMapper;

    /**
     * 分页查询书籍
     * @param bookPageQueryDTO
     * 除了page，limit外，其余字段进行模糊查询
     * @return
     */
    @Override
    public PageResult pageQuery(BookPageQueryDTO bookPageQueryDTO) {
        int pageNum = bookPageQueryDTO.getPageNum();
        int pageSize = bookPageQueryDTO.getPageSize();
        Page<Book> pageBook = new Page<>(pageNum, pageSize);
        // 构建查询条件
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        // 模糊查询title,author,isbn
        if (bookPageQueryDTO.getTitle() != null && !"".equals(bookPageQueryDTO.getTitle())) {
            wrapper.like("title", bookPageQueryDTO.getTitle());
        }
        if (bookPageQueryDTO.getAuthor() != null && !"".equals(bookPageQueryDTO.getAuthor())) {
            wrapper.like("author", bookPageQueryDTO.getAuthor());
        }
        if (bookPageQueryDTO.getIsbn() != null && !"".equals(bookPageQueryDTO.getIsbn())) {
            wrapper.like("isbn", bookPageQueryDTO.getIsbn());
        }
        // 执行查询
        Page<Book> page = this.page(pageBook, wrapper);
        return new PageResult(page.getTotal(), page.getRecords());
    }

    /**
     * 更新书籍状态
     * @param status
     * @param id
     */
    @Override
    public void updateBookStatus(Integer status, Long id) {
        UpdateWrapper<Book> wrapper = new UpdateWrapper<>();
        wrapper.set("status", status == 1 ? 0 : 1).eq("id", id);
        this.update(wrapper);
    }

    @Override
    public BookVO queryById(Integer id) {
        Book book = this.getById(id);
        BookVO bookVO = new BookVO();
        BeanUtil.copyProperties(book, bookVO);
        // 分类与状态字段需要查询和填入
        // 分类需要查询book_category表
        BookCategory bookCategory =
                bookCategoryMapper.selectById(book.getCategoryId());
        bookVO.setCategory(bookCategory.getName());
        System.out.println("bookCategory = "+bookCategory.getName());
        // 设置bookCategory字段
        bookVO.setBookCategory(bookCategoryMapper.selectById(bookCategory.getId()));
        // 状态需要填入statusConstant常量类
        bookVO.setStatus(book.getStatus() == 1
                ? StatusConstant.BOOK_STATUS_AVAILABLE
                : StatusConstant.BOOK_STATUS_UNAVAILABLE);
        return bookVO;
    }

    /**
     * 更新书籍信息
     * @param book
     * @return
     */
    @Override
    public void updateBook(Book book) {
        this.updateById(book);
    }
}
