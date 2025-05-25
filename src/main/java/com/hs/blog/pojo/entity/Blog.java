package com.hs.blog.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("blog")
public class Blog {
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("title")
    private String title;

    @TableField("sub_title")
    private String subTitle;

    @TableField("content")
    private String content;

    @TableField("user_id")
    private Integer userId;

    @TableField("category_id")
    private Integer categoryId;

    // 分为 草稿: 0
    //    已发布: 1
    @TableField("status")
    private Integer status;

    // 分为 未锁定: 0
    //       锁定: 1
    @TableField("lock_status")
    private Integer lockStatus;

    @TableField("view_count")
    private Integer viewCount;

//    @TableField("like_count")
//    private Integer likeCount;
//
//    @TableField("star_count")
//    private Integer starCount;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDeleted;
}