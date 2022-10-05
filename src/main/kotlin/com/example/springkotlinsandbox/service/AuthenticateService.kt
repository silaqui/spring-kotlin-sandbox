package com.example.springkotlinsandbox.service

import com.example.springkotlinsandbox.data.model.AuthenticationRequest
import com.example.springkotlinsandbox.data.model.AuthenticationResponse
import com.example.springkotlinsandbox.util.JwtUtil
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AuthenticateService(
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: UserDetailsService,
    private val jwtUtil: JwtUtil
) {

    @Throws(BadCredentialsException::class)
    fun createAuthenticationToken(request: AuthenticationRequest): AuthenticationResponse {
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(request.username, request.password))
        val user = userDetailsService.loadUserByUsername(request.username)
        val generateToken = jwtUtil.generateToken(user)
        return AuthenticationResponse(generateToken)
    }

}