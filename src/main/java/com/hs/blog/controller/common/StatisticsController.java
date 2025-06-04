package com.hs.blog.controller.common;

import com.hs.blog.result.Result;
import com.hs.blog.service.IBlogCategoryService;
import com.hs.blog.service.IBlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@Tag(name = "统计接口",description = "统计接口")
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private IBlogService blogService;

    @Autowired
    private IBlogCategoryService blogCategoryService;

    /**
     * 根据时间范围返回博客发布和阅读量的趋势数据
     * @return
     */
    @GetMapping("/getContentTrend")
    @Operation(summary = "根据时间范围返回博客发布和阅读量的趋势数据")
    public Result<Map<String, Object>> getContentTrend(
            @RequestParam(defaultValue = "week") String range){
        return blogService.getContentTrend(range);
    }

    /**
     * 获取博客分类统计数据
     * @return
     */
    @GetMapping("/getBlogCategoryStatistics")
    @Operation(summary = "获取博客分类统计数据")
    public Result<List<Map<String, Object>>> getBlogCategoryStatistics(){
        return blogCategoryService.getBlogCategoryStatistics();
    }

}
