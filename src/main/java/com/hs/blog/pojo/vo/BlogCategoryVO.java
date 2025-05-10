package com.hs.blog.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BlogCategoryVO implements Serializable {
    private Integer id;
    private String name;
    private Integer parentId;
    private List<BlogCategoryVO> children; // 子分类集合
}
