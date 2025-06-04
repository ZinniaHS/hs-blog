package com.hs.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hs.blog.constant.MessageConstant;
import com.hs.blog.mapper.BlogCategoryMapper;
import com.hs.blog.mapper.BlogMapper;
import com.hs.blog.pojo.dto.SaveBlogCategoryDTO;
import com.hs.blog.pojo.entity.*;
import com.hs.blog.pojo.entity.BlogCategory;
import com.hs.blog.pojo.vo.BlogCategoryVO;
import com.hs.blog.pojo.vo.BlogCategoryVO;
import com.hs.blog.result.Result;
import com.hs.blog.service.IBlogCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogCategoryServiceImpl
    extends ServiceImpl<BlogCategoryMapper, BlogCategory> implements IBlogCategoryService {

    @Autowired
    private BlogCategoryMapper blogCategoryMapper;
    @Autowired
    private BlogMapper blogMapper;
    
    /**
     * 查询所有的博客分类
     * @return
     */
    @Override
    public List<BlogCategoryVO> getBlogCategory() {
        // 查询所有分类
        List<BlogCategory> categoryList = blogCategoryMapper.selectList(null);

        // 转换并构建树形结构
        return buildCategoryTree(categoryList);
    }

    /**
     * 新增博客类型
     * @param saveBlogCategoryDTO
     * @return
     */
    @Override
    public Result saveCategory(SaveBlogCategoryDTO saveBlogCategoryDTO) {
        BlogCategory blogCategory = new BlogCategory();
        BeanUtils.copyProperties(saveBlogCategoryDTO, blogCategory);
        this.save(blogCategory);
        return Result.success();
    }

    /**
     * 删除博客分类
     * @param id
     * @param parentId
     * @return
     */
    @Override
    public Result deleteBlogCategory(Integer id, Integer parentId) {
        // 首先判断要删除的是一级分类还是二级分类
        if(parentId == null){
            // 如果是一级分类，要删除需满足条件，该一级分类下没有二级分类
            QueryWrapper<BlogCategory> wrapper = new QueryWrapper<>();
            wrapper.eq("parent_id", id);
            if(blogCategoryMapper.selectCount(wrapper) == 0){
                // 没有二级分类，可以删除
                this.removeById(id);
                System.out.println("一级分类下没有二级分类，可以删除");
                return Result.success(MessageConstant.DELETE_BLOG_CATEGORY_SUCCESS);
            }else{
                // 有二级分类，不能删除
                System.out.println("一级分类下有二级分类，不能删除");
                return Result.error(MessageConstant.EXIST_SECOND_CATEGORY);
            }
        }else {
            // 如果是二级分类，需要查询博客表，判断该二级分类下是否有博客
            QueryWrapper<Blog> wrapper = new QueryWrapper<>();
            wrapper.eq("category_id", id);
            if(blogMapper.selectCount(wrapper) == 0){
                // 没有博客，可以删除
                this.removeById(id);
                return Result.success(MessageConstant.DELETE_BLOG_CATEGORY_SUCCESS);
            }else {
                // 有博客，不能删除
                return Result.error(MessageConstant.EXIST_BLOGS);
            }
        }
    }

    /**
     * 更新博客分类信息（名称）
     * @param blogCategory
     * @return
     */
    @Override
    public Result editBlogCategory(BlogCategory blogCategory) {
        this.updateById(blogCategory);
        return Result.success();
    }

    /**
     * 获取博客分类统计数据
     * @return
     */
    @Override
    public Result<List<Map<String, Object>>> getBlogCategoryStatistics() {
        // 获取所有分类
        List<BlogCategory> categories = this.list();

        // 构建结果
        List<Map<String, Object>> result = new ArrayList<>();

        for (BlogCategory category : categories) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", category.getName());

            // 统计该分类下的博客数量
            int count = blogMapper.countByCategoryId(category.getId());

            item.put("value", count);
            result.add(item);
        }

        return Result.success(result);
    }

    private List<BlogCategoryVO> buildCategoryTree(List<BlogCategory> categories) {
        // 创建VO列表并拷贝属性
        List<BlogCategoryVO> voList = new ArrayList<>();
        categories.forEach(category -> {
            BlogCategoryVO vo = new BlogCategoryVO();
            BeanUtils.copyProperties(category, vo);
            voList.add(vo);
        });

        // 使用Map存储节点，便于快速查找
        Map<Integer, BlogCategoryVO> nodeMap = new HashMap<>();
        List<BlogCategoryVO> rootNodes = new ArrayList<>();

        // 第一次遍历：建立节点索引
        voList.forEach(vo -> {
            nodeMap.put(vo.getId(), vo);
            if (vo.getParentId() == null || vo.getParentId() == 0) {
                rootNodes.add(vo);
            }
        });

        // 第二次遍历：构建树形结构
        voList.forEach(vo -> {
            if (vo.getParentId() != null && vo.getParentId() != 0) {
                BlogCategoryVO parentNode = nodeMap.get(vo.getParentId());
                if (parentNode != null) {
                    if (parentNode.getChildren() == null) {
                        parentNode.setChildren(new ArrayList<>());
                    }
                    parentNode.getChildren().add(vo);
                }
            }
        });

        return rootNodes;
    }
}
