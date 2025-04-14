package com.hs.blog.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("book")
public class Book {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("isbn")
    private String isbn;

    @TableField("title")
    private String title;

    @TableField("author")
    private String author;

    @TableField("publisher")
    private String publisher;

    @TableField("publish_date")
    private LocalDate publishDate;

    @TableField("cover_url")
    private String coverUrl;

    @TableField("file_path")
    private String filePath;

    @TableField("category_id")
    private Integer categoryId;

    @TableField("description")
    private String description;

    @TableField("download_count")
    private Integer downloadCount;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
