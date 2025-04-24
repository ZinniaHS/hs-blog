package com.hs.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hs.blog.pojo.dto.BookPageQueryDTO;
import com.hs.blog.pojo.dto.SelectedCategoryBooksDTO;
import com.hs.blog.pojo.entity.Book;
import com.hs.blog.pojo.vo.BookVO;
import com.hs.blog.result.PageResult;
import com.hs.blog.result.Result;

import java.util.List;

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

    /**
     * 新增书籍信息
     * @param book
     * @return
     */
    void saveBook(Book book);

    /**
     * 批量删除书籍信息
     * @param ids 多个选中的图书id
     * @return
     */
    void batchDeleteBook(List<Integer> ids);

    /**
     * 根据一级，二级分类id分页查询图书
     * @return PageResult：总记录数和图书数据
     */
    PageResult getSelectedCategoryBooks(SelectedCategoryBooksDTO selectedCategoryBooksDTO);

}
