package com.example.springkotlinsandbox.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "jwt-security")
data class SecurityProperties(
    val signingKey: String
)