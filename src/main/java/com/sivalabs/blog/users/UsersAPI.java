package com.sivalabs.blog.users;

import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UsersAPI {
    private final UserService userService;

    UsersAPI(UserService userService) {
        this.userService = userService;
    }

    public Optional<User> findById(Long userId) {
        return userService.findById(userId);
    }

    public Optional<UserDto> findByEmail(String email) {
        return userService.findByEmail(email);
    }
}
