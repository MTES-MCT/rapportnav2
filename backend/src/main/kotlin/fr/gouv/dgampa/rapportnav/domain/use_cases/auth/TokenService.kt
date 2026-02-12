package fr.gouv.dgampa.rapportnav.domain.use_cases.auth

import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.entities.user.toAuthority
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.jwt.*
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

/**
 * This service creates and parses tokens.
 * It will do database operations.
 */
@Service
class TokenService(
    private val jwtDecoder: JwtDecoder,
    private val jwtEncoder: JwtEncoder,
    private val userRepository: IUserRepository,
) {
    private val logger = LoggerFactory.getLogger(TokenService::class.java)
    fun createToken(user: User): String {
        val jwsHeader = JwsHeader.with { "HS256" }.build()
        val claims = this.getClaims(user);
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).tokenValue
    }

    fun getClaims(user: User): JwtClaimsSet {
        return JwtClaimsSet.builder()
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plus(30L, ChronoUnit.DAYS))
            .subject(user.email)
            .claim("userId", user.id)
            .claim("authorities", user.roles.map { it.toAuthority() })
            .claim("roles", user.roles)
            .build()
    }

    fun parseToken(token: String): User? {
        return try {
            // Decode the JWT token
            val jwt = jwtDecoder.decode(token)

            // Extract userId from the claims
            val userId = (jwt.claims["userId"] as? Number)?.toLong()
                ?: throw IllegalArgumentException("Invalid userId in token")

            // Retrieve the user from the repository
            val user = userRepository.findById(userId.toInt())
                ?: throw IllegalArgumentException("User not found with ID: $userId")

            user
        } catch (e: Exception) {
            logger.debug("Token parsing failed: {}", e.message)
            null
        }
    }


}
