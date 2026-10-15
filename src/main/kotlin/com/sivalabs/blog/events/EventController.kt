package com.sivalabs.blog.events

import com.sivalabs.blog.shared.PagedResult
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/events")
@Tag(name = "Events API")
internal class EventController(private val eventService: EventService) {

    @GetMapping
    fun findEvents(@RequestParam(value = "page", defaultValue = "1") page: Int): PagedResult<EventDto> =
        eventService.findEvents(page)
}
