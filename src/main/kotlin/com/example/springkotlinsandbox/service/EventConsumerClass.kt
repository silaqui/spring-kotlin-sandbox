package com.example.springkotlinsandbox.service

import com.example.springkotlinsandbox.data.model.TestEvent
import mu.KotlinLogging
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Service

@Service
class EventConsumerClass : ApplicationListener<TestEvent> {

    private val logger = KotlinLogging.logger {}

    override fun onApplicationEvent(event: TestEvent) {
        logger.info { "Received event - ${event.data}" }
    }
}