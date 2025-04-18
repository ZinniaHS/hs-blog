package com.hs.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hs.blog.pojo.dto.BookPageQueryDTO;
import com.hs.blog.pojo.entity.Book;
import com.hs.blog.pojo.vo.BookVO;
import com.hs.blog.result.PageResult;
import com.hs.blog.result.Result;

public interface IBookService extends IService<Book> {

    /**
     * 分页查询书籍
     * @param bookPageQueryDTO
     * 除了page，limit外，其余字段进行模糊查询
     * @return
     */
    PageResult pageQuery(BookPageQueryDTO bookPageQueryDTO);

    /**
     * 更新书籍状态
     * @param status
     * @param id
     */
    void updateBookStatus(Integer status, Long id);

    /**
     * 根据id查询书籍信息
     * @param id
     * @return 返回BookVO对象
     */
    BookVO queryById(Integer id);

    /**
     * 更新书籍信息
     * @param book
     * @return
     */
    void updateBook(Book book);
}
