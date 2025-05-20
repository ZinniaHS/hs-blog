package com.hs.blog.pojo.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BlogViewEvent {
    private Integer blogId;
}
