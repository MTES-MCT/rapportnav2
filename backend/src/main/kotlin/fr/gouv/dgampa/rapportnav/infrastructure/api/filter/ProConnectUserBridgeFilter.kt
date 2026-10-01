package fr.gouv.dgampa.rapportnav.infrastructure.api.filter

import fr.gouv.dgampa.rapportnav.domain.entities.user.toAuthority
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.FindByEmail
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

/**
 * Bridges a ProConnect (OIDC) session principal into the domain [User] principal the rest
 * of the app expects (`SecurityContextHolder...principal as? User`).
 *
 * Runs only for `/api/` requests so that the OIDC logout flow keeps the original
 * [OAuth2AuthenticationToken] (needed to build the ProConnect end-session request).
 * The user is reloaded fresh from DB on every request — same security philosophy as
 * [CustomAuthenticationFilter] for the JWT path. Only the current request's SecurityContext
 * is mutated; the session still stores the original OIDC authentication.
 */
@Component
@ConditionalOnProperty(value = ["rapportnav.oidc.enabled"], havingValue = "true")
class ProConnectUserBridgeFilter(
    private val findByEmail: FindByEmail,
) : OncePerRequestFilter() {
    private val logger = LoggerFactory.getLogger(ProConnectUserBridgeFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val authentication = SecurityContextHolder.getContext().authentication

        if (request.requestURI.startsWith("/api/") &&
            authentication is OAuth2AuthenticationToken &&
            authentication.principal is OidcUser
        ) {
            val email = (authentication.principal as OidcUser).email
            val user = email?.let { findByEmail.execute(it) }

            if (user == null || user.disabledAt != null) {
                logger.warn("ProConnect session no longer authorized for ${email ?: "unknown"}")
                SecurityContextHolder.clearContext()
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
                return
            }

            val authorities = user.roles.map { SimpleGrantedAuthority(it.toAuthority().toString()) }
            // IMPORTANT: set a NEW context for this request rather than mutating the one
            // returned by getContext(). That instance is the very object stored in the HTTP
            // session, so mutating it in place would overwrite the session's OIDC
            // authentication with our domain User — breaking ProConnect end-session on logout.
            val context = SecurityContextHolder.createEmptyContext()
            context.authentication = UsernamePasswordAuthenticationToken(user, "", authorities)
            SecurityContextHolder.setContext(context)
        }

        filterChain.doFilter(request, response)
    }
}
