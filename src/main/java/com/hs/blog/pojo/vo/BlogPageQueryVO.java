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
    private String username;
    private String categoryName;
    private Integer viewCount;
    private Integer starCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
