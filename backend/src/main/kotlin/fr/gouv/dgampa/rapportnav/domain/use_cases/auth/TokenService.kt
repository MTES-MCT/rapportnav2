package fr.gouv.dgampa.rapportnav.domain.use_cases.auth

import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository
import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.jwt.*
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.security.oauth2.jwt.JwtException
import org.springframework.security.oauth2.jwt.JwsHeader
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
            .expiresAt(Instant.now().plus(15L, ChronoUnit.DAYS))
            .subject(user.email)
            .claim("userId", user.id)
            // SECURITY NOTE: roles are included for FRONTEND UI/ROUTING convenience only.
            // The backend NEVER uses these claims for authorization decisions.
            // On each request, parseToken() loads the user fresh from database,
            // ensuring current roles are used for actual permission checks.
            .claim("roles", user.roles)
            .build()
    }

    fun parseToken(token: String): User {
        val jwt = decodeToken(token)
        val userId = extractUserId(jwt)
        val user = findUser(userId)
        validateUserStatus(user)
        return user
    }

    private fun decodeToken(token: String): Jwt {
        try {
            return jwtDecoder.decode(token)
        } catch (e: JwtException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.INVALID_TOKEN_EXCEPTION,
                message = "Invalid or expired token"
            )
        }
    }

    private fun extractUserId(jwt: Jwt): Int {
        val userId = (jwt.claims["userId"] as? Number)?.toInt()
            ?: throw BackendUsageException(
                code = BackendUsageErrorCode.INVALID_TOKEN_EXCEPTION,
                message = "Invalid userId in token"
            )
        return userId
    }

    private fun findUser(userId: Int): User {
        return userRepository.findById(userId)
            ?: throw BackendUsageException(
                code = BackendUsageErrorCode.USER_NOT_FOUND_EXCEPTION,
                message = "User not found"
            )
    }

    private fun validateUserStatus(user: User) {
        if (user.disabledAt != null) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.USER_ACCOUNT_DISABLED_EXCEPTION,
                message = "User account is disabled or deleted"
            )
        }
    }


}
