package fr.gouv.dgampa.rapportnav.config

import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class CustomAuthenticationFilter(
    private val tokenService: TokenService
) : OncePerRequestFilter() {

    private val logger = LoggerFactory.getLogger(CustomAuthenticationFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                val token = authHeader.substring(7)
                val user = tokenService.parseToken(token)

                if (user != null) {
                    logger.debug("Custom Filter - User authenticated: {}", user.id)
                    logger.debug("Custom Filter - Roles: {}", user.roles)

                    val grantedAuthorities = user.roles.map { role ->
                        SimpleGrantedAuthority("ROLE_$role")
                    }

                    val authentication = UsernamePasswordAuthenticationToken(
                        user,
                        "",
                        grantedAuthorities
                    )

                    SecurityContextHolder.getContext().authentication = authentication

                    logger.debug("Custom Filter - Authentication set in SecurityContext")
                    logger.debug("Custom Filter - Granted Authorities: {}", grantedAuthorities)
                }
            } catch (e: Exception) {
                logger.debug("Custom Filter - Error parsing token: {}", e.message)
                // Clear any existing authentication
                SecurityContextHolder.clearContext()
            }
        }

        filterChain.doFilter(request, response)
    }
}
