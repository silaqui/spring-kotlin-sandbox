package com.example.springkotlinsandbox.service

import com.example.springkotlinsandbox.data.model.TestEvent
import mu.KotlinLogging
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service

@Service
class AlternativeEventConsumerClass {

    private val logger = KotlinLogging.logger {}

    @EventListener
    fun onEvent(event: TestEvent) {
        logger.info { "Received event in alternative service - ${event.data}" }
    }

}