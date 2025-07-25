package com.sivalabs.blog.users.core.models;

import java.time.Instant;

public record JwtToken(String token, Instant expiresAt) {}
