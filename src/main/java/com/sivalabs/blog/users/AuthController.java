package com.sivalabs.blog.users;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authManager;
    private final UserService userService;
    private final TokenHelper tokenHelper;

    AuthController(AuthenticationManager authManager,
                   UserService userService,
                   TokenHelper tokenHelper) {
        this.authManager = authManager;
        this.userService = userService;
        this.tokenHelper = tokenHelper;
    }

    @PostMapping("/api/login")
    LoginResponse login(@RequestBody @Valid LoginRequest req) {
        log.info("Login request for email: {}", req.email());
        var user = userService
                .findByEmail(req.email())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
        var authentication = new UsernamePasswordAuthenticationToken(req.email(), req.password());
        authManager.authenticate(authentication);
        var authToken = tokenHelper.generateToken(user.email());
        return new LoginResponse(authToken);
    }

    record LoginRequest(
            @NotEmpty(message = "Email is required")
            @Email(message = "Invalid email address")
            String email,
            @NotEmpty(message = "Password is required")
            String password) {}

    record LoginResponse(String token) {}
}