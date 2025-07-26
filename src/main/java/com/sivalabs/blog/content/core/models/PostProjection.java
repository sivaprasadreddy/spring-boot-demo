package com.sivalabs.blog.content.core.models;

import java.time.LocalDateTime;

public interface PostProjection {
    Long getId();

    String getTitle();

    String getSlug();

    String getContent();

    UserProjection getCreatedBy();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

    interface UserProjection {
        Long getId();

        String getName();

        String getEmail();
    }
}
