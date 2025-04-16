package com.hs.blog.pojo.dto;

import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class BookPageQueryDTO implements Serializable {

    @Schema(description = "当前页码", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private int pageNum;

    @Schema(description = "每页大小", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
    private int pageSize;

    @Schema(description = "书名（模糊查询）", example = "Java")
    private String title;

    @Schema(description = "ISBN号（模糊查询）")
    private String isbn;

    @Schema(description = "作者（模糊查询）")
    private String author;

    @Schema(description = "图书状态 0下架，1上架")
    private int status;

    @TableLogic
    private Integer isDeleted;  // 逻辑删除标记字段
}
