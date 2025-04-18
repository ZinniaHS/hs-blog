package com.hs.blog.pojo.vo;

import com.hs.blog.pojo.entity.BookCategory;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BookCategoryVO implements Serializable {
    private Integer id;
    private String name;
    private Integer parentId;
    private List<BookCategoryVO> children; // 子分类集合
}
