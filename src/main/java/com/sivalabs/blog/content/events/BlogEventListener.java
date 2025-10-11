package com.sivalabs.blog.content.events;

import com.sivalabs.blog.content.core.PostEventService;
import com.sivalabs.blog.notification.EmailService;
import com.sivalabs.blog.shared.entities.PostEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
class BlogEventListener {
    private final EmailService emailService;
    private final PostEventService postEventService;

    BlogEventListener(EmailService emailService, PostEventService postEventService) {
        this.emailService = emailService;
        this.postEventService = postEventService;
    }

    @ApplicationModuleListener
    void handle(PostPublishedEvent event) {
        PostEvent postEvent = new PostEvent(event.title(), event.slug(), event.createdAt());
        postEventService.save(postEvent);

        String subject = "New Post Published: " + event.title();
        String content =
                """
                New Post Published: <a href="%s">%s</a>
                %s
                """
                        .formatted(event.slug(), event.title(), event.content());
        emailService.send(subject, content);
    }
}
