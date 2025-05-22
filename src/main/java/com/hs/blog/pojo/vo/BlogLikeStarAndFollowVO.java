package com.hs.blog.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class BlogLikeStarAndFollowVO implements Serializable {

    // 已点赞为true，未点赞为false
    private boolean isLiked;
    // 已收藏为true，未收藏为false
    private boolean isStarred;
    // 已关注为true，未关注为false
    private boolean isFollowed;

}
