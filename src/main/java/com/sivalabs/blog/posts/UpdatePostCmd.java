package com.sivalabs.blog.posts;

public record UpdatePostCmd(Long id, String title, String slug, String content) {}
