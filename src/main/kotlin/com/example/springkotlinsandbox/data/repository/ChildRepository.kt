package com.example.springkotlinsandbox.data.repository

import com.example.springkotlinsandbox.data.entity.Child
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChildRepository : JpaRepository<Child, Int>