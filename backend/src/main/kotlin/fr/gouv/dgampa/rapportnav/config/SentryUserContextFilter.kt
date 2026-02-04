package fr.gouv.dgampa.rapportnav.config

import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import io.sentry.Sentry
import io.sentry.protocol.User as SentryUser
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

/**
 * Filter that sets Sentry user context from the authenticated user.
 * This allows correlating Sentry errors with specific users for debugging.
 *
 * GDPR Compliance:
 * - Only pseudonymized identifiers (user ID, service ID) are sent to Sentry
 * - Direct PII (email, name) is NOT sent to comply with data minimization (Article 5.1c)
 * - Legal basis: Legitimate interest in error tracking (Article 6.1f)
 * - To get user details, query the internal database using the user ID
 *
 * Added to SecurityConfig filter chains after authentication filters.
 */
@Component
class SentryUserContextFilter : OncePerRequestFilter() {

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.requestURI
        // Only run on /api/ paths, except login
        if (!path.startsWith("/api/")) return true
        if (path == "/api/v1/auth/login") return true
        return false
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            setSentryUserContext()
            filterChain.doFilter(request, response)
        } finally {
            clearSentryContext()
        }
    }

    private fun setSentryUserContext() {
        val authentication = SecurityContextHolder.getContext().authentication ?: return

        when (authentication.principal) {
            is User -> setUserContext(authentication.principal as User)
            is String -> setApiKeyContext(authentication)
        }
    }

    private fun setUserContext(user: User) {
        Sentry.setUser(SentryUser().apply {
            id = user.id?.toString()
        })
        user.serviceId?.let { Sentry.setTag("service_id", it.toString()) }
        Sentry.setTag("roles", user.roles.joinToString(",") { it.name })
    }

    private fun setApiKeyContext(authentication: Authentication) {
        @Suppress("UNCHECKED_CAST")
        val details = authentication.details as? Map<String, Any?>
        val publicId = details?.get("publicId")?.toString()
        Sentry.setUser(SentryUser().apply {
            id = publicId
        })
        Sentry.setTag("auth_type", "api_key")
        publicId?.let { Sentry.setTag("api_key_public_id", it) }
    }

    private fun clearSentryContext() {
        Sentry.setUser(null)
        Sentry.removeTag("service_id")
        Sentry.removeTag("roles")
        Sentry.removeTag("auth_type")
        Sentry.removeTag("api_key_public_id")
    }
}