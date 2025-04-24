package com.hs.blog.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class SelectedCategoryBooksDTO implements Serializable {
    @Schema(description = "当前页码", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private int pageNum;

    @Schema(description = "每页大小", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private int pageSize;

    @Schema(description = "一级分类ID")
    private int firstId;

    @Schema(description = "二级分类ID")
    private int secondId;
}
