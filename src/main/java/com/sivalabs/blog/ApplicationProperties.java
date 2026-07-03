package com.sivalabs.blog;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "blog")
@Validated
public record ApplicationProperties(
        @NotEmpty
        String supportEmail,
        @NotNull
        @Positive
        Integer postsPageSize,
        @NotEmpty
        String tokenSecret,
        @NotNull
        @Min(5)
        @DefaultValue("30")
        Long tokenExpiresInMinutes
        ) {
}
