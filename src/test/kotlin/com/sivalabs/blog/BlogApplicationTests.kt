package com.sivalabs.blog

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

class BlogApplicationTests : AbstractIT() {
    private val log = LoggerFactory.getLogger(javaClass)

    @Test
    fun shouldLoadContext() {
        log.info("ApplicationContext loaded successfully")
    }
}
