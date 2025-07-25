package com.sivalabs.blog.content.core.models;

public record CreatePostCmd(String title, String slug, String content, Long createdBy) {}
