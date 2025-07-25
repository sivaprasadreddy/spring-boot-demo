package com.sivalabs.blog.content.core.models;

public record CreateCommentCmd(String name, String email, String content, Long postId) {}
