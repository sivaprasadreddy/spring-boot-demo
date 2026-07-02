package com.sivalabs.blog.users;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Auth API")
class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final JwtTokenHelper jwtTokenHelper;
    private final UserMapper userMapper;

    AuthController(UserService userService, JwtTokenHelper jwtTokenHelper, UserMapper userMapper) {
        this.userService = userService;
        this.jwtTokenHelper = jwtTokenHelper;
        this.userMapper = userMapper;
    }

    @PostMapping("/login")
    LoginResponse login(@RequestBody @Valid LoginRequest req) {
        log.info("Login request for email: {}", req.email());
        var user = userService
                .login(req.email(), req.password())
                .map(userMapper::toUser)
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        var jwtToken = jwtTokenHelper.generateToken(user);
        return new LoginResponse(
                jwtToken.token(),
                jwtToken.expiresAt(),
                user.name(),
                user.email(),
                user.role().name());
    }

    public record LoginRequest(
            @NotEmpty(message = "Email is required") @Email(message = "Invalid email address") String email,

            @NotEmpty(message = "Password is required") String password) {}

    public record LoginResponse(String token, Instant expiresAt, String name, String email, String role) {}
}
