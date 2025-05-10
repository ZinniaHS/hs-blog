package com.hs.blog.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SaveBlogCategoryDTO {

    @Schema(description = "一级分类ID")
    private Integer parentId;

    @Schema(description = "分类名称")
    private String name;

}
