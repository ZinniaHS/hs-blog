package com.hs.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hs.blog.mapper.BookCategoryMapper;
import com.hs.blog.pojo.entity.BookCategory;
import com.hs.blog.service.IBookCategoryService;
import org.springframework.stereotype.Service;

@Service
public class BookCategoryServiceImpl
        extends ServiceImpl<BookCategoryMapper, BookCategory> implements IBookCategoryService {
}
