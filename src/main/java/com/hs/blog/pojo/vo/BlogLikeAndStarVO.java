package com.hs.blog.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class BlogLikeAndStarVO implements Serializable {

    // 已点赞为true，未点赞为false
    private boolean isLiked;
    // 已收藏为true，未收藏为false
    private boolean isStarred;

}
