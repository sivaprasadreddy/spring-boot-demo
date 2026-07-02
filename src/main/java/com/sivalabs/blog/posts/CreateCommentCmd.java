package com.sivalabs.blog.posts;

public record CreateCommentCmd(String name, String email, String content, Long postId) {}
