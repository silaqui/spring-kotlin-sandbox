package com.example.springkotlinsandbox.controller

import com.example.springkotlinsandbox.service.PublicService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

const val PUBLIC_API = "/public"

@Controller
@RequestMapping(PUBLIC_API)
class PublicController(
    private val publicService: PublicService
) {

    @GetMapping
    fun greeting(): ResponseEntity<String> {
        val greeting = publicService.greeting()
        return ResponseEntity.ok(greeting)
    }

}