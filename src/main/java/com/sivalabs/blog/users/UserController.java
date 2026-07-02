package com.sivalabs.blog.users;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users API")
class UserController {
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @PostMapping
    ResponseEntity<UserDto> createUser(@RequestBody @Valid User user) {
        var userDto = userService.createUser(user);
        return ResponseEntity.status(CREATED.value()).body(userDto);
    }
}
