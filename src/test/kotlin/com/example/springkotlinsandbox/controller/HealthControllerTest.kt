package com.example.springkotlinsandbox.controller

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class HealthControllerTest {

    private val tested = HealthController()

    @Test
    fun healthTest() {
        // When
        val actual = tested.health()

        // Then
        Assertions.assertEquals("OK", actual)
    }
}
