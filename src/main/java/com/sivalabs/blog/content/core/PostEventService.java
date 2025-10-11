package com.sivalabs.blog.content.core;

import com.sivalabs.blog.shared.entities.PostEvent;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PostEventService {
    private static final Logger log = LoggerFactory.getLogger(PostEventService.class);
    private final PostEventRepository postEventRepository;

    public PostEventService(PostEventRepository postEventRepository) {
        this.postEventRepository = postEventRepository;
    }

    @PostConstruct
    void init() {
        long count = postEventRepository.count();
        if (count == 0) {
            log.info("No post events found. Loading sample data...");
            var post = new PostEvent("Sample Post", "sample-post", LocalDateTime.now());
            postEventRepository.save(post);
        }
        log.info("Total post events: {}", count);
    }

    public void save(PostEvent postEvent) {
        postEventRepository.save(postEvent);
    }
}
