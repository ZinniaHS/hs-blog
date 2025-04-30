package com.hs.blog.pojo.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "用户注册对象")
public class UserRegisterDTO implements Serializable {
    // 唯一邮箱
    private String email;
    private String captcha;
    private String username;
    private String password;
}
