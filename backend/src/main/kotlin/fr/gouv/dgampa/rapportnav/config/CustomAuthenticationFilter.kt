package fr.gouv.dgampa.rapportnav.config

import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class CustomAuthenticationFilter(
    private val tokenService: TokenService
) : OncePerRequestFilter() {

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
                    println("Custom Filter - User: $user")
                    println("Custom Filter - Roles: ${user.roles}")

                    val grantedAuthorities = user.roles.map { role ->
                        SimpleGrantedAuthority("ROLE_$role")
                    }

                    val authentication = UsernamePasswordAuthenticationToken(
                        user,
                        "",
                        grantedAuthorities
                    )

                    SecurityContextHolder.getContext().authentication = authentication

                    println("Custom Filter - Authentication set in SecurityContext")
                    println("Custom Filter - Granted Authorities: $grantedAuthorities")
                }
            } catch (e: Exception) {
                println("Custom Filter - Error parsing token: ${e.message}")
                // Clear any existing authentication
                SecurityContextHolder.clearContext()
            }
        }

        filterChain.doFilter(request, response)
    }
}
