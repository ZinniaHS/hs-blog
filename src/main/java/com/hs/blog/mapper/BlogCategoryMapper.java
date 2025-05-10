package com.hs.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hs.blog.pojo.entity.BlogCategory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogCategoryMapper extends BaseMapper<BlogCategory> {
}
