package com.sivalabs.blog;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "blog")
@Validated
public record ApplicationProperties(
        @NotEmpty
        String supportEmail,
        @NotNull
        @Positive
        Integer postsPageSize) {
}
