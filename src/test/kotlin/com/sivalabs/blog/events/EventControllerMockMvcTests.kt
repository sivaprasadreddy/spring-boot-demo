package com.sivalabs.blog.events

import com.sivalabs.blog.AbstractIT
import com.sivalabs.blog.posts.PostPublishedEvent
import com.sivalabs.blog.shared.PagedResult
import java.time.LocalDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.context.ApplicationEventPublisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus.OK

class EventControllerMockMvcTests : AbstractIT() {

    @Autowired
    private lateinit var eventPublisher: ApplicationEventPublisher

    @Test
    fun shouldGetEvents() {
        repeat(3) { i ->
            eventPublisher.publishEvent(PostPublishedEvent("Title $i", "slug-$i", "content $i", LocalDateTime.now()))
        }

        val result = mockMvcTester.get().uri("/api/events").exchange()
        assertThat(result).hasStatus(OK)
            .bodyJson()
            .satisfies({ json ->
                assertThat(json).extractingPath("$.currentPageNo").isEqualTo(1)
                assertThat(json).extractingPath("$.totalElements").asNumber()
                    .satisfies({ total -> assertThat(total.toLong()).isGreaterThanOrEqualTo(3L) })
                assertThat(json).extractingPath("$.data[*].eventType").asArray()
                    .contains(PostPublishedEvent::class.java.name)
            })
    }

    @Test
    fun shouldReturnEmptyPageBeyondLastPage() {
        val result = mockMvcTester.get().uri("/api/events?page=1000").exchange()
        assertThat(result).hasStatus(OK)
            .bodyJson()
            .convertTo(PagedResult::class.java)
            .satisfies({ page: PagedResult<*> ->
                assertThat(page.data()).isEmpty()
                assertThat(page.currentPageNo()).isEqualTo(1000)
            })
    }
}
