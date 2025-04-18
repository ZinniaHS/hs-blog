package com.hs.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hs.blog.pojo.entity.BookCategory;
import com.hs.blog.pojo.vo.BookCategoryVO;

import java.util.List;

/**
 * 查询所有的图书分类
 * @return BookCategoryVO
 */
public interface IBookCategoryService extends IService<BookCategory> {
    List<BookCategoryVO> getBookCategory();
}
