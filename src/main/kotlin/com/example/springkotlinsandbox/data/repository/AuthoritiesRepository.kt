package com.example.springkotlinsandbox.data.repository

import com.example.springkotlinsandbox.data.entity.Authorities
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthoritiesRepository : CrudRepository<Authorities, Int> {}