package com.hs.blog.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField("publish_date")
    private LocalDate publishDate;

    @TableField("cover_url")
    private String coverUrl;

    @TableField("file_path")
    private String filePath;

    @TableField("category_id")
    private Integer categoryId;

    // 0：下架，1：上架
    @TableField("status")
    private Integer status;

    @TableField("description")
    private String description;

    @TableField("download_count")
    private Integer downloadCount;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDeleted;
}
