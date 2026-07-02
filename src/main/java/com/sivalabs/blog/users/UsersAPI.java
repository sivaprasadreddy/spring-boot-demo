package com.sivalabs.blog.users;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UsersAPI {
    private final UserService userService;

    public UsersAPI(UserService userService) {
        this.userService = userService;
    }

    public Optional<User> findUserById(Long userId) {
        return userService.findById(userId);
    }

    public List<User> findAllUsers() {
        return userService.findAllUsers();
    }
}
