package com.example.springkotlinsandbox.service

import com.example.springkotlinsandbox.data.model.TestEvent
import mu.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class EventPublisherService(
    private val applicationEventPublisher: ApplicationEventPublisher,
) {

    private val logger = KotlinLogging.logger {}

    fun publish() {
        logger.info("Publishing event")
        val event = TestEvent(this, LocalDateTime.now().toString())
        applicationEventPublisher.publishEvent(event)
    }
}