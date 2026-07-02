package com.sivalabs.blog.posts;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ContentAPI {
    private final PostService postService;

    public ContentAPI(PostService postService) {
        this.postService = postService;
    }

    public List<Post> findPostsCreatedBetween(LocalDateTime startOfWeek, LocalDateTime end) {
        return postService.findPostsCreatedBetween(startOfWeek, end);
    }
}
