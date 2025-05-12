package com.hs.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hs.blog.constant.MessageConstant;
import com.hs.blog.context.CustomUserDetails;
import com.hs.blog.mapper.BlogMapper;
import com.hs.blog.pojo.dto.BlogDTO;
import com.hs.blog.pojo.dto.BlogPageQueryDTO;
import com.hs.blog.pojo.entity.Blog;
import com.hs.blog.pojo.entity.Book;
import com.hs.blog.pojo.vo.BlogPageQueryVO;
import com.hs.blog.result.PageResult;
import com.hs.blog.result.Result;
import com.hs.blog.service.IBlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BlogServiceImpl
        extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

    /**
     * 新增博客
     * @param blogDTO
     * @return
     */
    @Override
    public Result saveBlog(BlogDTO blogDTO) {
        System.out.println("================"+blogDTO);
        String userId = null;
        // 获取当前登录用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        }
        if(userId == null)
            return Result.error(MessageConstant.NOT_LOGIN);
        Blog blog = new Blog();
        BeanUtils.copyProperties(blogDTO, blog);
        blog.setUserId(Integer.valueOf(userId));
        this.save(blog);
        return Result.success();
    }

    @Override
    public PageResult pageQueryForUser(BlogPageQueryDTO blogPageQueryDTO) {
        int pageNum = blogPageQueryDTO.getPageNum();
        int pageSize = blogPageQueryDTO.getPageSize();
        Page<Blog> pageBlog = new Page<>(pageNum, pageSize);
        // 构建查询条件
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        // 根据创建时间降序排序
        wrapper.orderByDesc("create_time");
        // 筛选已发布的博客
        wrapper.eq("status", 1);
        // 筛选未被锁定的博客
        wrapper.eq("lock_status", 0);
        // 根据关键字模糊查询title,subTitle,content字段
        if (blogPageQueryDTO.getKeyWord() != null && !"".equals(blogPageQueryDTO.getKeyWord())) {
            wrapper.like("title", blogPageQueryDTO.getKeyWord()).or()
                    .like("sub_title", blogPageQueryDTO.getKeyWord()).or()
                    .like("content", blogPageQueryDTO.getKeyWord());
        }
        // 执行查询
        Page<Blog> page = this.page(pageBlog, wrapper);

        return new PageResult(page.getTotal(), page.getRecords());
    }

}
