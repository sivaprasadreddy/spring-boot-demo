package com.sivalabs.blog.posts;

import java.time.LocalDateTime;

record PostPublishedEvent(String title, String slug, String content, LocalDateTime createdAt) {}
