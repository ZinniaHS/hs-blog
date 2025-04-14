package com.hs.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hs.blog.mapper.BookMapper;
import com.hs.blog.pojo.entity.Book;
import com.hs.blog.result.Result;
import com.hs.blog.service.IBookService;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements IBookService {

}
