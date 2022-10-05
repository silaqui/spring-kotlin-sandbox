package com.example.springkotlinsandbox.data.repository

import com.example.springkotlinsandbox.data.entity.Parent
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ParentRepository : JpaRepository<Parent, Int>