package com.hs.blog.handle;

import com.hs.blog.pojo.event.BlogViewEvent;
import com.hs.blog.mapper.BlogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class BlogViewEventHandler {

    @Autowired
    private BlogMapper blogMapper;

    @Async("dbUpdateExecutor") // 指定独立线程池
    @EventListener
    public void handleBlogViewEvent(BlogViewEvent event) {
        // 批量更新时建议用INCREMENT_COUNT变量（此处简化）
        blogMapper.incrementViewCount(event.getBlogId());
    }

}
