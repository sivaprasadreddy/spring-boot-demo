package com.sivalabs.blog.content.events;

public record PostPublishedEvent(String title, String slug, String content) {}
