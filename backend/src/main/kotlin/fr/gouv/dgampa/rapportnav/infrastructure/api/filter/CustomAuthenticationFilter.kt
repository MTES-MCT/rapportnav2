package fr.gouv.dgampa.rapportnav.infrastructure.api.filter

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
                // Do NOT clear the context here. A stale/expired Bearer token must not wipe an
                // authentication already established upstream — in particular the ProConnect (OIDC)
                // session restored by Spring's SecurityContextHolderFilter. Clearing it would make
                // a leftover JWT in the client break session-based login (users/me -> 401, forcing
                // a second ProConnect sign-in). We only ever set the context on success, so a
                // failed parse has nothing of ours to roll back.
            }
        }

        filterChain.doFilter(request, response)
    }
}
