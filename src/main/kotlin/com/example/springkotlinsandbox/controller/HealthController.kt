package com.example.springkotlinsandbox.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

const val HEALTH_API = "/health"

@RestController
@RequestMapping(HEALTH_API)
class HealthController {

    @Operation(
        summary = "Check server status.",
        description = "Returns 200 if successful",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful Operation"),
        ]
    )
    @GetMapping
    fun health(): String {
        return "OK"
    }
}