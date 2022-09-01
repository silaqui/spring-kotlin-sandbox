package com.example.springkotlinsandbox.controller

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class HealthControllerMappingTest {

    private val tested: HealthController = mockk(relaxed = true)

    private val mockMvc = MockMvcBuilders
        .standaloneSetup(tested)
        .build()

    @Test
    @Throws(Exception::class)
    fun healthRoutingTest() {
        // When
        mockMvc.perform(MockMvcRequestBuilders.get("/health"))

        // Then
        verify { tested.health() }
    }
}