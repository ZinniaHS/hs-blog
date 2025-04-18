package com.hs.blog.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.hs.blog.pojo.entity.BookCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookVO implements Serializable {

    private Long id;

    private String isbn;

    private String title;

    private String author;

    private String publisher;

    private LocalDate publishDate;

    private String coverUrl;

    private String filePath;
    // 为了获得分类id以及其父分类id
    private BookCategory bookCategory;
    // 分类名
    private String category;
    // 状态：上架、已下架，会引用StatusConstant
    private String status;

    private String description;

    private Integer downloadCount;
}
