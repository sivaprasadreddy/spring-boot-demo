package com.sivalabs.blog.users;

import java.time.Instant;

public record JwtToken(String token, Instant expiresAt) {}
