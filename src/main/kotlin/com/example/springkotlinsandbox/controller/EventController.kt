package com.example.springkotlinsandbox.controller

import com.example.springkotlinsandbox.service.EventPublisherService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class EventController(
    private val eventPublisherService: EventPublisherService
) {

    @Operation(
        summary = "Trigger publisher service.",
        description = "Returns 200 if successful",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful Operation"),
        ]
    )
    @GetMapping("/event")
    fun health(): String {
        eventPublisherService.publish()
        return "OK"
    }
}