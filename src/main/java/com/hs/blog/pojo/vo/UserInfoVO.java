package com.hs.blog.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserInfoVO implements Serializable {

    private Integer id;
    private String username;
    private String email;
    private String avatarUrl;
    private String description;
    private String phone;
    private boolean isMyPage;

}
