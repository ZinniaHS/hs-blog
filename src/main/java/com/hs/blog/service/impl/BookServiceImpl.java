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
import com.hs.blog.pojo.dto.SelectedCategoryBooksDTO;
import com.hs.blog.pojo.entity.Book;
import com.hs.blog.pojo.entity.BookCategory;
import com.hs.blog.pojo.vo.BookVO;
import com.hs.blog.result.PageResult;
import com.hs.blog.result.Result;
import com.hs.blog.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        // 根据创建时间降序排序
        wrapper.orderByDesc("create_time");
        // 模糊查询title,author,isbn
        if (bookPageQueryDTO.getKeyWord() != null && !"".equals(bookPageQueryDTO.getKeyWord())) {
            wrapper.like("title", bookPageQueryDTO.getKeyWord()).or()
                    .like("author", bookPageQueryDTO.getKeyWord()).or()
                    .like("isbn", bookPageQueryDTO.getKeyWord()).or()
                    .like("description", bookPageQueryDTO.getKeyWord()).or()
                    .like("publisher", bookPageQueryDTO.getKeyWord());
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

    /**
     * 新增书籍信息
     * @param book
     * @return
     */
    @Override
    public void saveBook(Book book) {
        this.save(book);
    }

    /**
     * 批量删除书籍信息
     * @param ids 多个选中的图书id
     * @return
     */
    @Override
    public void batchDeleteBook(List<Integer> ids) {
        this.removeBatchByIds(ids);
    }

    /**
     * 根据一级，二级分类id分页查询图书
     * @return PageResult：总记录数和图书数据
     */
    @Override
    public PageResult getSelectedCategoryBooks(SelectedCategoryBooksDTO selectedCategoryBooksDTO) {
        //  前端选择的一级，二级分类id
        //  约定如果一级分类id是0则查询全部图书
        //  如果二级分类id是0的话则查询一级分类下的所有图书
        int pageNum = selectedCategoryBooksDTO.getPageNum();
        int pageSize = selectedCategoryBooksDTO.getPageSize();
        Page<Book> pageBook = new Page<>(pageNum, pageSize);
        int firstId = selectedCategoryBooksDTO.getFirstId();
        int secondId = selectedCategoryBooksDTO.getSecondId();
        System.out.println("======="+pageNum);
        System.out.println("======="+pageSize);
        System.out.println("======="+firstId);
        System.out.println("======="+secondId);
        QueryWrapper<Book> wrapper = new QueryWrapper<>();
        if(secondId != 0){
            // 二级分类不为0，则查询二级分类下的图书
            wrapper.eq("category_id", secondId);
        }else if(firstId != 0 && secondId == 0){
            // 一级分类不为0且二级分类为0，则查询一级分类下的所有图书
            // 首先在book_category表中查询一级分类下的所有二级分类id
            QueryWrapper<BookCategory> categoryWrapper = new QueryWrapper<>();
            categoryWrapper.eq("parent_id", firstId);
            List<BookCategory> categories = bookCategoryMapper.selectList(categoryWrapper);
            for(BookCategory category : categories)
                wrapper.eq("category_id", category.getId()).or();
        }
        // 如果以上情况都不是，只剩下一级分类为0，则查询全部图书
        Page<Book> page = this.page(pageBook, wrapper);
        return new PageResult(page.getTotal(), page.getRecords());
    }
}
