package com.example.springkotlinsandbox.filter

import com.example.springkotlinsandbox.util.JwtUtil
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtRequestFilter(
    private val userDetailsService: UserDetailsService,
    private val jwtUtil: JwtUtil
) : OncePerRequestFilter() {

    private val authorizationHeader = "Authorization"
    private val jwtPrefix = "Bearer "

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        getToken(request)?.also { token ->
            jwtUtil.extractUsername(token)?.also { username ->
                if (SecurityContextHolder.getContext().authentication == null) {
                    userDetailsService.loadUserByUsername(username).also {
                        if (jwtUtil.validateToken(token, it)) {
                            setSecurityContext(request, it)
                        }
                    }
                }
            }
        }
        filterChain.doFilter(request, response)
    }

    private fun getToken(request: HttpServletRequest): String? {
        val header: String? = request.getHeader(authorizationHeader)
        return if (header?.contains(jwtPrefix) == true) {
            header.substring(7)
        } else {
            null
        }
    }

    private fun setSecurityContext(request: HttpServletRequest, userDetails: UserDetails) {
        val token = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
        token.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = token
    }
}