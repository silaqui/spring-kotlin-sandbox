package com.example.springkotlinsandbox.controller

import com.example.springkotlinsandbox.service.AdminService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

const val ADMIN_API = "/admin"

@Controller
@RequestMapping(ADMIN_API)
class AdminController(
    private val adminService: AdminService
) {

    @GetMapping
    fun greeting(): ResponseEntity<String> {
        val greeting = adminService.greeting()
        return ResponseEntity.ok(greeting)
    }

}