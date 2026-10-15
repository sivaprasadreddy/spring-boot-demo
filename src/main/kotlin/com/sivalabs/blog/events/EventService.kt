package com.sivalabs.blog.events

import com.sivalabs.blog.shared.PagedResult
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.modulith.events.core.EventPublicationRepository
import org.springframework.modulith.events.core.TargetEventPublication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
internal class EventService(private val eventPublicationRepository: EventPublicationRepository) {

    @Transactional(readOnly = true)
    fun findEvents(pageNo: Int): PagedResult<EventDto> {
        val page = if (pageNo < 1) 1 else pageNo
        val publications = (eventPublicationRepository.findCompletedPublications() +
                eventPublicationRepository.findIncompletePublications())
            .sortedByDescending { it.publicationDate }
        val pageable = PageRequest.of(page - 1, PAGE_SIZE)
        val content = publications
            .drop(pageable.offset.toInt())
            .take(PAGE_SIZE)
            .map { it.toEventDto() }
        return PagedResult.from(PageImpl(content, pageable, publications.size.toLong()))
    }

    private fun TargetEventPublication.toEventDto() = EventDto(
        id = identifier,
        eventType = event.javaClass.name,
        listener = targetIdentifier.value,
        status = status,
        publicationDate = publicationDate,
        completionDate = completionDate.orElse(null),
        payload = event,
    )

    companion object {
        private const val PAGE_SIZE = 10
    }
}
