package com.hs.blog.pojo.vo;

import lombok.Builder;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BlogVO implements Serializable {

    private Long id;
    private String title;
    private String subTitle;
    private String content;
    private String username;
    private String userAvatar;
    private Integer categoryId;
    private String categoryName;
    private Integer viewCount;
    private Integer likeCount;
    private Integer starCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
