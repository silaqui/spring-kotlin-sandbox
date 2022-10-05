package com.example.springkotlinsandbox.service

import com.example.springkotlinsandbox.data.entity.Roles
import com.example.springkotlinsandbox.data.repository.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import javax.transaction.Transactional


@Service("userDetailsService")
@Transactional
class UserDetailsServiceImpl(
    private val userRepository: UserRepository
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(name: String): UserDetails {
        val user = userRepository.findByUsername(name)
        if (user != null) {
            return User(
                user.username,
                user.password,
                getGrantedAuthorities(user.roles)
            )
        } else {
            throw UsernameNotFoundException("User not found")
        }
    }

    private fun getGrantedAuthorities(roles: Collection<Roles>): List<GrantedAuthority> {
        return roles
            .map { r -> r.authorities }
            .flatten()
            .distinct()
            .map { a -> SimpleGrantedAuthority(a.authority) }
    }
}