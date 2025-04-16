package com.hs.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hs.blog.mapper.BookMapper;
import com.hs.blog.pojo.dto.BookPageQueryDTO;
import com.hs.blog.pojo.entity.Book;
import com.hs.blog.result.PageResult;
import com.hs.blog.result.Result;
import com.hs.blog.service.IBookService;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements IBookService {

    /**
     * 分页查询书籍
     * @param bookPageQueryDTO
     * 除了page，limit外，其余字段进行模糊查询
     * @return
     */

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
        // 只查询上架的书籍
        wrapper.eq("status", 1);
        // 执行查询
        Page<Book> page = this.page(pageBook, wrapper);
        return new PageResult(page.getTotal(), page.getRecords());
    }
}
