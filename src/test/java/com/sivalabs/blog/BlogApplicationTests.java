package com.sivalabs.blog;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class BlogApplicationTests extends AbstractIT {

    private static final Logger log = LoggerFactory.getLogger(BlogApplicationTests.class);

    @Test
    void shouldLoadContext() {
        log.info("ApplicationContext loaded successfully");
    }
}
