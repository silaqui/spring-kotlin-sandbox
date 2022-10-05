package com.example.springkotlinsandbox.data.repository

import com.example.springkotlinsandbox.data.entity.Users
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<Users, Int> {
    fun findByUsername(username: String): Users?
}