package com.hs.blog.pojo.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "用户登录对象")
public class UserLoginDTO {
    private String email;
    private String password;
}
