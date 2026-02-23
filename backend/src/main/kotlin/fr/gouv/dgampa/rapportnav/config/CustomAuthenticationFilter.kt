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
                // extract token
                val token = authHeader.substring(7)

                // through token, revalidate user with db data
                // do not use token data directly
                val user = tokenService.parseToken(token)

                // set authorities from roles
                val grantedAuthorities = user.roles.map { role ->
                    SimpleGrantedAuthority("ROLE_$role")
                }

                // set user and authorities to secu context
                val authentication = UsernamePasswordAuthenticationToken(
                    user,
                    "",
                    grantedAuthorities
                )
                SecurityContextHolder.getContext().authentication = authentication
            } catch (e: Exception) {
                logger.error("Error while authenticating user", e)
                SecurityContextHolder.clearContext()
            }
        }

        filterChain.doFilter(request, response)
    }
}
