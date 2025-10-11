package com.sivalabs.blog.shared.entities;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "post_events")
public class PostEvent {
    @Id
    private String id;

    @Indexed(unique = true)
    private String slug;

    private String title;
    private LocalDateTime publishedAt;

    public PostEvent() {}

    public PostEvent(String title, String slug, LocalDateTime publishedAt) {
        this.title = title;
        this.slug = slug;
        this.publishedAt = publishedAt;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSlug() {
        return slug;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }
}
