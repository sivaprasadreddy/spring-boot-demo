package com.sivalabs.blog.users;

public record CreateUserCmd(String name, String email, String password, Role role) {}
