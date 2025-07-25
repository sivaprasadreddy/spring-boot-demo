package com.sivalabs.blog.content.core.models;

public record UpdatePostCmd(Long id, String title, String slug, String content) {}
