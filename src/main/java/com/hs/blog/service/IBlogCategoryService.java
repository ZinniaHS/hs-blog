package com.hs.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hs.blog.pojo.dto.SaveBlogCategoryDTO;
import com.hs.blog.pojo.entity.BlogCategory;
import com.hs.blog.pojo.vo.BlogCategoryVO;
import com.hs.blog.result.Result;

import java.util.List;
import java.util.Map;

public interface IBlogCategoryService extends IService<BlogCategory> {

    /**
     * 查询所有的博客分类
     * @return
     */
    List<BlogCategoryVO> getBlogCategory();

    /**
     * 新增博客类型
     * @param saveBlogCategoryDTO
     * @return
     */
    Result saveCategory(SaveBlogCategoryDTO saveBlogCategoryDTO);

    /**
     * 删除博客分类
     * @param id
     * @param parentId
     * @return
     */
    Result deleteBlogCategory(Integer id, Integer parentId);

    /**
     * 更新博客分类信息（名称）
     * @param blogCategory
     * @return
     */
    Result editBlogCategory(BlogCategory blogCategory);

    /**
     * 获取博客分类统计数据
     * @return
     */
    Result<List<Map<String, Object>>> getBlogCategoryStatistics();
}
