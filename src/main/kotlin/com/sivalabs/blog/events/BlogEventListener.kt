package com.sivalabs.blog.events

import com.sivalabs.blog.notification.EmailService
import com.sivalabs.blog.posts.PostPublishedEvent
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Component

@Component
internal class BlogEventListener(private val emailService: EmailService) {

    @ApplicationModuleListener
    fun handle(event: PostPublishedEvent) {
        val subject = "New Post Published: ${event.title}"
        val content = """
                New Post Published: <a href="${event.slug}">${event.title}</a>
                ${event.content}
                """
        emailService.send(subject, content)
    }
}
