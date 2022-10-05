package com.example.springkotlinsandbox.controller

import com.example.springkotlinsandbox.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

const val USER_API = "/user"

@Controller
@RequestMapping(USER_API)
class UserController(
    private val userService: UserService
) {

    @GetMapping
    fun greeting(): ResponseEntity<String> {
        val greeting = userService.greeting()
        return ResponseEntity.ok(greeting)
    }

}