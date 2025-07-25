package com.sivalabs.blog.content.core.models;

import com.sivalabs.blog.users.core.models.UserProjection;
import java.time.LocalDateTime;

public interface PostProjection {
    Long getId();

    String getTitle();

    String getSlug();

    String getContent();

    UserProjection getCreatedBy();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();
}
