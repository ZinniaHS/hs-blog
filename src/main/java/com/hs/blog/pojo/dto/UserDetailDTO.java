package com.hs.blog.pojo.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserDetailDTO implements Serializable {

    private Integer id;
    private String username;
    private String avatarUrl;
    private String description;
    private String phone;

}
