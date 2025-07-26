package com.sivalabs.blog.content;

import com.sivalabs.blog.content.core.PostService;
import com.sivalabs.blog.shared.entities.Post;
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
