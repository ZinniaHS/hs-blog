package com.hs.blog.pojo.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "博客保存对象")
public class BlogDTO {

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("副标题")
    private String subTitle;

    @ApiModelProperty("博客内容")
    private String content;

    @ApiModelProperty("分类id")
    private Integer categoryId;

    // 分为 草稿: 0
    //    已发布: 1
    @ApiModelProperty("发布状态")
    private Integer status;
}
