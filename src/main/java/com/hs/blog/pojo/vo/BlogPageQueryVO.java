package com.hs.blog.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BlogPageQueryVO implements Serializable {

    private Long id;
    private String title;
    private String subTitle;
    private String userId;
    private String username;
    private String userAvatar;
    private String categoryName;
    private Integer viewCount;
    private Integer likeCount;
    private Integer starCount;
    // 分为 草稿: 0
    //    已发布: 1
    private Integer status;
    // 分为 未锁定: 0
    //       锁定: 1
    private Integer lockStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
