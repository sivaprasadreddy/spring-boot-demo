package com.sivalabs.blog.content.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class BlogEventPublisher {
    private static final Logger log = LoggerFactory.getLogger(BlogEventPublisher.class);
    private final ApplicationEventPublisher eventPublisher;

    BlogEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publish(PostPublishedEvent event) {
        log.info("Publishing event: {}", event);
        eventPublisher.publishEvent(event);
    }
}
