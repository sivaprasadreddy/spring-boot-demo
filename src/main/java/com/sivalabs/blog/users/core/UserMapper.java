package com.sivalabs.blog.users.core;

import com.sivalabs.blog.shared.entities.User;
import com.sivalabs.blog.users.core.models.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toUser(User entity);
}
