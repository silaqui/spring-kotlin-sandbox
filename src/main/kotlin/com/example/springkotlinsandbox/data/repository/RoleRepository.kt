package com.example.springkotlinsandbox.data.repository

import com.example.springkotlinsandbox.data.entity.Roles
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : CrudRepository<Roles, Int> {}