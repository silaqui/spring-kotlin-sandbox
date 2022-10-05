package com.example.springkotlinsandbox.controller

import com.example.springkotlinsandbox.data.model.AuthenticationRequest
import com.example.springkotlinsandbox.data.model.AuthenticationResponse
import com.example.springkotlinsandbox.service.AuthenticateService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

const val AUTHENTICATE_API = "/authenticate"

@Controller
@RequestMapping(AUTHENTICATE_API)
class AuthenticateController(
    private val authenticationService: AuthenticateService
) {

    @PostMapping
    fun createAuthenticationToken(@RequestBody request: AuthenticationRequest): ResponseEntity<AuthenticationResponse> {
        return try {
            val token = authenticationService.createAuthenticationToken(request)
            ResponseEntity.ok(token)
        } catch (e: BadCredentialsException) {
            ResponseEntity(HttpStatus.FORBIDDEN)
        }
    }
}