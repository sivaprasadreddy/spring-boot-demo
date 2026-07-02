package com.sivalabs.blog.posts;

public record CreatePostCmd(String title, String slug, String content, Long createdBy) {}
