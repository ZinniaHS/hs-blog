package com.hs.blog.task;

import com.hs.blog.constant.RedisConstant;
import com.hs.blog.mapper.BlogMapper;
import com.hs.blog.pojo.entity.Blog;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Slf4j
public class DataInitializer {

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

//    @PostConstruct // 在项目启动后，依赖注入完成后执行
//    public void initViewCountsInRedis() {
//        log.info("开始初始化博客浏览量到 Redis...");
//
//        // 1. 查询数据库中所有的博客
//        List<Blog> blogs = blogMapper.selectList(null);
//
//        if (blogs == null || blogs.isEmpty()) {
//            log.info("数据库中没有博客数据，无需初始化。");
//            return;
//        }
//
//        // 2. 将数据添加到 Redis ZSET
//        // 使用 pipeline 批量操作，性能更高
//        stringRedisTemplate.executePipelined((RedisCallback<Object>) connection -> {
//            for (Blog blog : blogs) {
//                if (blog.getId() != null && blog.getViewCount() != null) {
//                    connection.zAdd(
//                            RedisConstant.BLOG_VIEW_COUNT_KEY.getBytes(),
//                            blog.getViewCount(),
//                            blog.getId().toString().getBytes()
//                    );
//                }
//            }
//            return null;
//        });
//
//        log.info("成功初始化 {} 条博客浏览量到 Redis。", blogs.size());
//    }

    // 使用原子布尔值确保线程安全，并保证只执行一次
    private final AtomicBoolean initialized = new AtomicBoolean(false);

    /**
     * 一个只在项目启动后执行一次的定时任务。
     * initialDelay = 1000L 表示在应用启动完成后，延迟1000毫秒（1秒）开始执行。
     * fixedRate = Long.MAX_VALUE 表示下一次执行将在一个非常非常久远的时间之后，
     * 结合内部的布尔判断，这实际上确保了它只会被触发一次。
     */
    @Scheduled(initialDelay = 1000L, fixedRate = Long.MAX_VALUE)
    public void initViewCountsInRedis() {
        // 使用 compareAndSet 原子操作来判断和设置标志位
        // 如果 initialized 是 false，则将其设置为 true，并执行代码块
        if (initialized.compareAndSet(false, true)) {
            log.info("【一次性初始化任务】开始执行：初始化博客浏览量到 Redis...");

            try {
                // 1. 查询数据库中所有的博客
                List<Blog> blogs = blogMapper.selectList(null);

                if (blogs == null || blogs.isEmpty()) {
                    log.info("数据库中没有博客数据，无需初始化。");
                    return;
                }

                // 2. 将数据添加到 Redis ZSET (使用 pipeline 批量操作)
                stringRedisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                    for (Blog blog : blogs) {
                        if (blog.getId() != null && blog.getViewCount() != null) {
                            connection.zAdd(
                                    RedisConstant.BLOG_VIEW_COUNT_KEY.getBytes(),
                                    blog.getViewCount(),
                                    blog.getId().toString().getBytes()
                            );
                        }
                    }
                    return null;
                });

                log.info("【一次性初始化任务】成功完成！共初始化 {} 条博客浏览量到 Redis。", blogs.size());

            } catch (Exception e) {
                // 如果出现异常（比如你提到的数据库连接问题），需要记录下来
                log.error("【一次性初始化任务】执行失败！原因: {}", e.getMessage());
                // 失败后，可以选择将标志位重置，以便下次重启时可以重试
                initialized.set(false);
            }
        }
    }


}
