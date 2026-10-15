package com.sivalabs.blog.posts;

import java.time.LocalDateTime;

public record PostPublishedEvent(String title, String slug, String content, LocalDateTime createdAt) {}
