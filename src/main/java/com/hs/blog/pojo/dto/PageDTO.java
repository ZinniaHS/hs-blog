package com.hs.blog.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PageDTO {
    @ApiModelProperty(value = "总条数")
    private Integer total;
    @ApiModelProperty(value = "总页数")
    private Integer pages;
    @ApiModelProperty(value = "结果集合")
    private List<?> list;
}
