package com.hs.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hs.blog.pojo.dto.BookCategoryPageQueryDTO;
import com.hs.blog.pojo.dto.SaveBookCategoryDTO;
import com.hs.blog.pojo.entity.BookCategory;
import com.hs.blog.pojo.vo.BookCategoryVO;
import com.hs.blog.result.PageResult;
import com.hs.blog.result.Result;

import java.util.List;

/**
 * 查询所有的图书分类
 * @return BookCategoryVO
 */
public interface IBookCategoryService extends IService<BookCategory> {

    /**
     * 查询所有的图书分类
     * @return BookCategoryVO
     */
    List<BookCategoryVO> getBookCategory();

    /**
     * 分页查询图书类型信息
     * @param bookCategoryPageQueryDTO
     * @return
     */
    PageResult pageQuery(BookCategoryPageQueryDTO bookCategoryPageQueryDTO);

    /**
     * 新增图书类型
     * @param saveBookCategoryDTO
     * @return
     */
    void saveCategory(SaveBookCategoryDTO saveBookCategoryDTO);

    /**
     * 删除书籍分类
     * @param id，parentId
     * @return
     */
    Result deleteBookCategory(Integer id, Integer parentId);

    /**
     * 更新书籍分类信息（名称）
     * @param bookCategory
     * @return
     */
    Result editBookCategory(BookCategory bookCategory);
}
