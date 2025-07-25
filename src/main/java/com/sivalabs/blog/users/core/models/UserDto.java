package com.sivalabs.blog.users.core.models;

import com.sivalabs.blog.shared.models.Role;

public record UserDto(Long id, String name, String email, String password, Role role) {}
