package com.example.springkotlinsandbox.config

import com.example.springkotlinsandbox.data.entity.Authorities
import com.example.springkotlinsandbox.data.entity.Roles
import com.example.springkotlinsandbox.data.entity.Users
import com.example.springkotlinsandbox.data.repository.AuthoritiesRepository
import com.example.springkotlinsandbox.data.repository.RoleRepository
import com.example.springkotlinsandbox.data.repository.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component
class InitialDataLoader(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val authoritiesRepository: AuthoritiesRepository,
    private val passwordEncoder: PasswordEncoder
) : ApplicationRunner {

    @Transactional
    override fun run(args: ApplicationArguments) {
        val userPrivilege = authoritiesRepository.save(Authorities(authority = USER_AUTHORITY))
        val adminPrivilege = authoritiesRepository.save(Authorities(authority = ADMIN_AUTHORITY))

        val adminRoles =
            roleRepository.save(Roles(role = "ROLE_ADMIN", authorities = listOf(adminPrivilege, userPrivilege)))
        val userRoles = roleRepository.save(Roles(role = "ROLE_USER", authorities = listOf(userPrivilege)))

        userRepository.save(
            Users(
                username = "admin",
                password = passwordEncoder.encode("123"),
                roles = setOf(adminRoles)
            )
        )
        userRepository.save(
            Users(
                username = "user",
                password = passwordEncoder.encode("123"),
                roles = setOf(userRoles)
            )
        )
    }
}