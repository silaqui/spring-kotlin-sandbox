package com.example.springkotlinsandbox.util

import com.example.springkotlinsandbox.config.SecurityProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

const val hour = 60 * 60 * 1000

@Service
class JwtUtil(
    private val securityProperties: SecurityProperties
) {

    fun generateToken(userDetails: UserDetails): String {
        return Jwts.builder()
            .setClaims(HashMap())
            .setSubject(userDetails.username)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + hour * 10))
            .signWith(SignatureAlgorithm.HS256, securityProperties.signingKey)
            .compact()
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        return extractUsername(token) == userDetails.username && !isTokenExpired(token)
    }

    fun extractUsername(token: String): String? = extractClaim(token) { obj: Claims -> obj.subject }

    fun isTokenExpired(token: String): Boolean = extractExpiration(token)?.before(Date()) ?: false

    fun extractExpiration(token: String): Date? = extractClaim(token) { obj: Claims -> obj.expiration }

    private fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = Jwts
            .parser()
            .setSigningKey(securityProperties.signingKey)
            .parseClaimsJws(token)
            .body
        return claimsResolver(claims)
    }


}