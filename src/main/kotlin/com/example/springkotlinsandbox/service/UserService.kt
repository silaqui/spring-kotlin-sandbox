package com.example.springkotlinsandbox.service

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class UserService {

    fun greeting(): String {
        val auth = SecurityContextHolder.getContext().authentication
        return "Hello ${auth.name} :)"
    }
}