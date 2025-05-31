package com.hs.blog.pojo.vo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class TopBlogVO {
    private Integer id;
    private String title;
    private String subTitle;
    private String content;
    private Integer userId;
    private Integer categoryId;
    // 分为 草稿: 0
    //    已发布: 1
    private Integer status;
    // 分为 未锁定: 0
    //       锁定: 1
    private Integer lockStatus;
    private Integer viewCount;
    private Integer likeCount;
    private Integer starCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer isDeleted;
}
