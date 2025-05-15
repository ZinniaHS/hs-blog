package com.hs.blog.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class BlogPageQueryForOneDTO implements Serializable {

    private int userId;

    @Schema(description = "类型为已发布article，草稿draft")
    private String type;

    @Schema(description = "当前页码", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private int pageNum;

    @Schema(description = "每页大小", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private int pageSize;

    @Schema(description = "前端输入的关键词")
    private String keyWord;

    @Schema(description = "按发布时间排序，升序或降序")
    private String createTimeOrder;

    @Schema(description = "按访问量排序，升序或降序")
    private String viewCountOrder;

}
