package com.example.springkotlinsandbox.config

import com.example.springkotlinsandbox.controller.*
import com.example.springkotlinsandbox.filter.JwtRequestFilter
import com.example.springkotlinsandbox.service.UserDetailsServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val filter: JwtRequestFilter,
    private val userDetailsService: UserDetailsServiceImpl
) : WebSecurityConfigurerAdapter() {

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth
            .userDetailsService(userDetailsService)
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .addFilterBefore(filter, UsernamePasswordAuthenticationFilter::class.java)
            .csrf()
            .disable()
            .authorizeRequests()
            .antMatchers(AUTHENTICATE_API).permitAll()
            .antMatchers(HEALTH_API).permitAll()
            .antMatchers("$PUBLIC_API/**").permitAll()
            .antMatchers("$USER_API/**").hasAuthority(USER_AUTHORITY)
            .antMatchers("$ADMIN_API/**").hasAuthority(ADMIN_AUTHORITY)
            .antMatchers("/**").denyAll()
            .anyRequest()
            .authenticated()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    @Bean
    fun encoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    override fun authenticationManager(): AuthenticationManager = super.authenticationManager()
}