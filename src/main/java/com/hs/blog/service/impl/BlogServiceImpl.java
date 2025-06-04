package com.hs.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hs.blog.constant.MessageConstant;
import com.hs.blog.context.CustomUserDetails;
import com.hs.blog.pojo.dto.BlogPageQueryForOtherDTO;
import com.hs.blog.pojo.event.BlogViewEvent;
import com.hs.blog.mapper.BlogCategoryMapper;
import com.hs.blog.mapper.BlogMapper;
import com.hs.blog.mapper.UserMapper;
import com.hs.blog.pojo.dto.BlogDTO;
import com.hs.blog.pojo.dto.BlogPageQueryDTO;
import com.hs.blog.pojo.dto.BlogPageQueryForOneDTO;
import com.hs.blog.pojo.entity.*;
import com.hs.blog.pojo.vo.BlogLikeStarAndFollowVO;
import com.hs.blog.pojo.vo.BlogPageQueryVO;
import com.hs.blog.pojo.vo.BlogVO;
import com.hs.blog.result.PageResult;
import com.hs.blog.result.Result;
import com.hs.blog.service.IBlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BlogServiceImpl
        extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

    private static final String VIEW_COUNT_KEY = "blog:views";

    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private BlogCategoryMapper blogCategoryMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    /**
     * 新增博客
     * @param blogDTO
     * @return
     */
    @Override
    public Result saveBlog(BlogDTO blogDTO) {
        System.out.println("================"+blogDTO);
        int userId = getUserId();
        if(userId == -1)
            return Result.error(MessageConstant.NOT_LOGIN);
        Blog blog = new Blog();
        BeanUtils.copyProperties(blogDTO, blog);
        blog.setUserId(userId);
        this.save(blog);
        return Result.success();
    }

    /**
     * 客户端分页查询博客信息
     * @param blogPageQueryDTO
     * @return
     */
    @Override
    public PageResult queryBlogByPage(BlogPageQueryDTO blogPageQueryDTO) {
        Page<BlogPageQueryVO> page = new Page<>();
        page.setCurrent(blogPageQueryDTO.getPageNum());  // 设置当前页
        page.setSize(blogPageQueryDTO.getPageSize());    // 设置每页数量
        IPage<BlogPageQueryVO> res = blogMapper.queryBlogByPage(page, blogPageQueryDTO);
        System.out.println(res.getRecords());
        return new PageResult(res.getTotal(), res.getRecords());
    }

    /**
     * 根据id查询博客信息
     * @param id
     * @return
     */
    @Override
    public BlogVO queryById(Integer id) {
//        BlogVO blogVO = new BlogVO();
//        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("id", id);
//        Blog blog = blogMapper.selectOne(queryWrapper);
//        BeanUtils.copyProperties(blog, blogVO);
//        User user = userMapper.selectById(blog.getUserId());
//        blogVO.setUserAvatar(user.getAvatarUrl());
//        blogVO.setUsername(user.getUsername());
//        BlogCategory blogCategory = blogCategoryMapper.selectById(blog.getCategoryId());
//        blogVO.setCategoryName(blogCategory.getName());
//        return blogVO;
        return blogMapper.queryById(id);
    }

    /**
     * 根据用户id分页查询他的所有博客
     * @param blogPageQueryForOneDTO
     * @return
     */
    @Override
    public PageResult queryAllBlogsByUserId(BlogPageQueryForOneDTO blogPageQueryForOneDTO) {
        Page<BlogPageQueryVO> page = new Page<>();
        page.setCurrent(blogPageQueryForOneDTO.getPageNum());  // 设置当前页
        page.setSize(blogPageQueryForOneDTO.getPageSize());    // 设置每页数量
        IPage<BlogPageQueryVO> res = blogMapper.queryAllBlogsByUserId(page, blogPageQueryForOneDTO);
        return new PageResult(page.getTotal(), page.getRecords());
    }

    /**
     * 新增或修改博客
     * @param blog
     * @return
     */
    @Override
    public Result updateBlog(Blog blog) {
        blog.setUserId(getUserId());
        this.saveOrUpdate(blog);
        return Result.success();
    }

    /**
     * 删除博客
     * @param id
     * @return
     */
    @Override
    public Result deleteBlog(Integer id) {
        System.out.println(id);
        blogMapper.deleteById(id);
        return Result.success();
    }

    /**
     * 管理端分页查询博客信息
     * @param blogPageQueryDTO
     * @return
     */
    @Override
    public PageResult adminQueryBlogByPage(BlogPageQueryDTO blogPageQueryDTO) {
        Page<BlogPageQueryVO> page = new Page<>();
        page.setCurrent(blogPageQueryDTO.getPageNum());  // 设置当前页
        page.setSize(blogPageQueryDTO.getPageSize());    // 设置每页数量
        IPage<BlogPageQueryVO> res = blogMapper.adminQueryBlogByPage(page, blogPageQueryDTO);
        return new PageResult(res.getTotal(), res.getRecords());
    }

    /**
     * 更新博客锁定状态
     * @param lockStatus 当前博客锁定状态
     * @param id 博客id
     * @return
     */
    @Override
    public void updateBlogLockStatus(Integer lockStatus, Integer id) {
        UpdateWrapper<Blog> wrapper = new UpdateWrapper<>();
        wrapper.set("lock_status", lockStatus == 1 ? 0 : 1).eq("id", id);
        this.update(wrapper);
    }

    /**
     * 管理员端修改博客
     * @param blog
     * @return
     */
    @Override
    public void updateBlogForAdmin(Blog blog) {
        this.updateById(blog);
    }

    /**
     * 批量删除博客
     * @param ids 多个选中的博客id
     * @return
     */
    @Override
    public void batchDeleteBlog(List<Integer> ids) {
        this.removeByIds(ids);
    }

    /**
     * 博客浏览数量+1
     * @param id
     * @return
     */
    @Override
    public Result incrementViewCount(Integer id) {
        // 原子操作：阅读量+1（ZSCORE不存在时会自动创建）
        redisTemplate.opsForZSet().incrementScore(VIEW_COUNT_KEY, id.toString(), 1);
        // 发布异步事件更新数据库
        eventPublisher.publishEvent(new BlogViewEvent(id));
        return Result.success();
    }

    /**
     * 根据用户id，获取该用户所有关注者的博客
     * @return
     */
    @Override
    public PageResult getSubscription(BlogPageQueryForOtherDTO blogPageQueryForOtherDTO) {
        // 如果userId为-1则表示当前用户查询自己的关注的博主的博客
        if (blogPageQueryForOtherDTO.getUserId() == -1) {
            // 获取当前登录用户id
            blogPageQueryForOtherDTO.setUserId(getUserId());
        }
        Page<BlogPageQueryVO> page = new Page<>();
        page.setCurrent(blogPageQueryForOtherDTO.getPageNum());  // 设置当前页
        page.setSize(blogPageQueryForOtherDTO.getPageSize());    // 设置每页数量
        IPage<BlogPageQueryVO> res = blogMapper.getSubscription(page, blogPageQueryForOtherDTO);
        System.out.println(res.getRecords());
        return new PageResult(res.getTotal(), res.getRecords());
    }

    /**
     * 博客点赞数量+1
     * @param blogId
     * @return
     */
    @Override
    public Result incrementLikeCount(Integer blogId) {
        int userId = getUserId();
        blogMapper.incrementLikeCount(userId, blogId);
        return Result.success();
    }



    /**
     * 博客收藏数量+1
     * @param blogId
     * @return
     */
    @Override
    public Result incrementStarCount(Integer blogId) {
        int userId = getUserId();
        blogMapper.incrementStarCount(userId, blogId);
        return Result.success();
    }

    /**
     * 当前用户对博客点赞数量-1
     * @param blogId
     * @return
     */
    @Override
    public Result decrementLikeCount(Integer blogId) {
        int userId = getUserId();
        blogMapper.decrementLikeCount(userId, blogId);
        return Result.success();
    }

    /**
     * 当前用户对博客收藏数量-1
     * @param blogId
     * @return
     */
    @Override
    public Result decrementStarCount(Integer blogId) {
        int userId = getUserId();
        blogMapper.decrementStarCount(userId, blogId);
        return Result.success();
    }

    /**
     * 获取博客点赞和收藏以及当前用户对博主的关注状态
     * @param blogId
     * @return
     */
    @Override
    public Result<BlogLikeStarAndFollowVO> getLikeStarAndFollowStatus(Integer blogId, Integer bloggerId) {
        int userId = getUserId();
        BlogLikeStarAndFollowVO res =
                blogMapper.getLikeStarAndFollowStatus(userId, blogId, bloggerId);
        System.out.println("========================"+res);
        return Result.success(res);
    }

    /**
     * 根据用户id，获取该用户收藏的博客
     * @return
     */
    @Override
    public PageResult getStarBlogs(BlogPageQueryForOtherDTO blogPageQueryForOtherDTO) {
        // 如果userId为-1则表示当前用户查询自己的关注的博主的博客
        if (blogPageQueryForOtherDTO.getUserId() == -1) {
            // 获取当前登录用户id
            blogPageQueryForOtherDTO.setUserId(getUserId());
        }
        Page<BlogPageQueryVO> page = new Page<>();
        page.setCurrent(blogPageQueryForOtherDTO.getPageNum());  // 设置当前页
        page.setSize(blogPageQueryForOtherDTO.getPageSize());    // 设置每页数量
        IPage<BlogPageQueryVO> res = blogMapper.getStarBlogs(page, blogPageQueryForOtherDTO);
        return new PageResult(res.getTotal(), res.getRecords());
    }

    /**
     * 获取个人浏览量排行前五的博客
     * @return
     */
    @Override
    public List<Blog> getTopFiveBlogForOne(Integer userId) {
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.orderByDesc("view_count");
        List<Blog> res = this.list(wrapper);
        // 如果少于5条记录直接返回最大记录数
        return res.subList(0, Math.min(5, res.size()));
    }

    /**
     * 根据时间范围返回博客发布和阅读量的趋势数据
     * @return
     */
    @Override
    public Result<Map<String, Object>> getContentTrend(String range) {
        // 确定日期范围
        LocalDate endDate = LocalDate.now();
        LocalDate startDate;

        if ("week".equals(range)) {
            startDate = endDate.minusDays(6); // 近7天
        } else {
            startDate = endDate.minusDays(29); // 近30天
        }

        // 查询指定日期范围内的博客数据
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();

        // 获取所有符合条件的博客
        List<Blog> blogs = getBlogsByDateRange(startDateTime, endDateTime);

        // 准备返回数据
        List<String> dates = new ArrayList<>();
        List<Integer> articles = new ArrayList<>();
        List<Integer> views = new ArrayList<>();

        // 按日期分组统计
        Map<LocalDate, List<Blog>> blogsByDate = blogs.stream()
                .collect(Collectors.groupingBy(blog ->
                        blog.getCreateTime().toLocalDate()));

        // 计算每天的文章数和阅读量
        LocalDate currentDate = startDate;
        DateTimeFormatter formatter = "week".equals(range) ?
                DateTimeFormatter.ofPattern("E") : // 周一、周二等
                DateTimeFormatter.ofPattern("d日"); // 1日、2日等

        while (!currentDate.isAfter(endDate)) {
            // 添加日期
            String formattedDate = currentDate.format(formatter);
            dates.add(formattedDate);

            // 获取当天的博客
            List<Blog> dailyBlogs = blogsByDate.getOrDefault(currentDate, Collections.emptyList());

            // 当天发布的文章数
            articles.add(dailyBlogs.size());

            // 当天的阅读量总和
            int dailyViews = dailyBlogs.stream()
                    .mapToInt(Blog::getViewCount)
                    .sum();
            views.add(dailyViews);

            // 移动到下一天
            currentDate = currentDate.plusDays(1);
        }

        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("dates", dates);
        result.put("articles", articles);
        result.put("views", views);

        return Result.success(result);
    }

    /**
     * 获取指定日期范围内的博客数据
     * @param startTime
     * @param endTime
     * @return
     */
    public List<Blog> getBlogsByDateRange(LocalDateTime startTime, LocalDateTime endTime) {
        LambdaQueryWrapper<Blog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(Blog::getCreateTime, startTime)
                .lt(Blog::getCreateTime, endTime)
                .eq(Blog::getStatus, 1) // 仅统计已发布的文章
                .orderByAsc(Blog::getCreateTime);

        return this.list(queryWrapper);
    }

    /**
     * 获取浏览量排行前五的博客
     * @return
     */
    @Override
    public List<Blog> getTopFiveBlog() {
        try {
            // 1. 从Redis获取Top5的博客ID（字符串形式）
            Set<String> blogIdStrings = redisTemplate.opsForZSet()
                    .reverseRange(VIEW_COUNT_KEY, 0, 4);
            // 2. 无缓存时回源数据库
            if (CollectionUtils.isEmpty(blogIdStrings)) {
//                // 若数据库中无数据，Redis 也为空，后续请求会持续穿透到数据库，缓存空值以解决
//                redisTemplate.opsForValue().set("NULL_RESULT",
//                        "1", 5, TimeUnit.MINUTES); // 短期缓存空结果
                log.info("当前无缓存，将从数据库中查询");
                return fallbackToDatabase();
            }
            // 3. 转换为Integer类型ID
            List<Integer> blogIds = blogIdStrings.stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(id -> {
                        try {
                            Integer.parseInt(id); // 确保是Integer
                            return true;
                        } catch (NumberFormatException e) {
                            log.warn("Invalid blog ID in Redis: {}", id);
                            return false;
                        }
                    })
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            if (blogIds.isEmpty()) {
                log.info("没有在Redis中找到有效的博客ID，回源数据库");
                return fallbackToDatabase();
            }
            // 4. 批量查询博客详情
            List<Blog> blogs = blogMapper.selectBatchIds(blogIds);
            // 5. 按Redis顺序重排序
            return reorderByRedisRanking(blogIds, blogs);
        } catch (Exception e) {
            log.error("Error getting top five blogs from Redis, falling back to database", e);
            return fallbackToDatabase();
        }
    }

    /**
     * 查询数据库获取浏览量最高的五条博客
     * @return
     */
    private List<Blog> fallbackToDatabase() {
        LambdaQueryWrapper<Blog> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Blog::getViewCount)
                .last("LIMIT 5");
        return blogMapper.selectList(wrapper);
    }

    /**
     * 按Redis顺序重排序博客列表
     * @param blogIds
     * @param blogs
     * @return
     */
    private List<Blog> reorderByRedisRanking(List<Integer> blogIds, List<Blog> blogs) {
        Map<Integer, Blog> blogMap = blogs.stream()
                .collect(Collectors.toMap(Blog::getId, Function.identity()));
        return blogIds.stream()
                .map(blogMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 获取当前用户登录id
     * @return 返回用户id，不存在则返回-1
     */
    public synchronized Integer getUserId() {
        String userId = null;
        // 获取当前登录用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            userId = ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        }
        return userId != null ? Integer.valueOf(userId) : -1;
    }
}
