package com.hs.blog.task;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hs.blog.constant.RedisConstant;
import com.hs.blog.mapper.BlogMapper;
import com.hs.blog.pojo.entity.Blog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
public class ViewCountSyncTask {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private BlogMapper blogMapper;

    // 每 10 分钟执行一次
    @Scheduled(fixedRate = 1000 * 60 * 10)
    public void syncViewCountsToDb() {
        log.info("开始执行【浏览量】同步任务...");

        // 1. 从 Redis 获取所有博客的浏览量
        // 为了防止数据量过大导致内存问题，可以分批获取，但如果博客数量在几万内，一次性获取通常没问题
        Set<ZSetOperations.TypedTuple<String>> tuples = stringRedisTemplate.opsForZSet()
                .rangeWithScores(RedisConstant.BLOG_VIEW_COUNT_KEY, 0, -1);

        if (tuples == null || tuples.isEmpty()) {
            log.info("没有需要同步的浏览量数据。");
            return;
        }

        // 2. 遍历并更新数据库
        // 注意：逐条更新效率较低，如果数据量大，最好使用批量更新。
        for (ZSetOperations.TypedTuple<String> tuple : tuples) {
            String blogId = tuple.getValue();
            Double viewCount = tuple.getScore();
            if (blogId != null && viewCount != null) {
                Blog blog = new Blog();
                blog.setViewCount(viewCount.intValue());

                UpdateWrapper<Blog> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("id", Integer.parseInt(blogId));

                blogMapper.update(blog, updateWrapper);
            }
        }
        log.info("【浏览量】同步任务完成，共处理 {} 条数据。", tuples.size());
    }
}
