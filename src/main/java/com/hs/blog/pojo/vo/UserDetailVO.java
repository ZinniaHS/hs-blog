package com.hs.blog.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserDetailVO implements Serializable {

    private String username;
    private String email;
    private String avatarUrl;
    private String description;
    private String phone;

}
