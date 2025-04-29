package com.hs.blog.pojo.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "用户登录对象")
public class UserLoginDTO implements Serializable {
    // 唯一邮箱
    private String email;
    private String username;
    private String password;
}
