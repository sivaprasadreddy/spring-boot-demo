package com.sivalabs.blog.events

import java.time.Instant
import java.util.UUID
import org.springframework.modulith.events.EventPublication

data class EventDto(
    val id: UUID,
    val eventType: String,
    val listener: String,
    val status: EventPublication.Status,
    val publicationDate: Instant,
    val completionDate: Instant?,
    val payload: Any,
)
