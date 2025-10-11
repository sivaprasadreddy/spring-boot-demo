package com.sivalabs.blog.content.events;

import java.time.LocalDateTime;

public record PostPublishedEvent(String title, String slug, String content, LocalDateTime createdAt) {}
