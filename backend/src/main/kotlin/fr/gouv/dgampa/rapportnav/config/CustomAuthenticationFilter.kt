package fr.gouv.dgampa.rapportnav.config

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendForbiddenException
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.ProcessImpersonationRequest
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class CustomAuthenticationFilter(
    private val tokenService: TokenService,
    private val processImpersonationRequest: ProcessImpersonationRequest
) : OncePerRequestFilter() {

    private val logger = LoggerFactory.getLogger(CustomAuthenticationFilter::class.java)

    companion object {
        const val IMPERSONATE_SERVICE_HEADER = "X-Impersonate-Service-Id"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                val token = authHeader.substring(7)
                var user = tokenService.parseToken(token)

                // Delegate impersonation to use case
                val targetServiceId = request.getHeader(IMPERSONATE_SERVICE_HEADER)?.toIntOrNull()
                user = processImpersonationRequest.execute(
                    user = user,
                    targetServiceId = targetServiceId,
                    ipAddress = getClientIpAddress(request)
                )

                val authorities = user.roles.map { SimpleGrantedAuthority("ROLE_$it") }
                SecurityContextHolder.getContext().authentication =
                    UsernamePasswordAuthenticationToken(user, "", authorities)
            } catch (e: BackendForbiddenException) {
                logger.warn("Unauthorized impersonation attempt: ${e.message}")
                sendForbiddenResponse(response, e.message ?: "Impersonation not authorized")
                return
            } catch (e: Exception) {
                logger.error("Error while authenticating user", e)
                SecurityContextHolder.clearContext()
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun sendForbiddenResponse(response: HttpServletResponse, message: String) {
        response.status = HttpStatus.FORBIDDEN.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        val errorBody = mapOf("error" to "Forbidden", "message" to message)
        response.writer.write(ObjectMapper().writeValueAsString(errorBody))
        response.writer.flush()
    }

    private fun getClientIpAddress(request: HttpServletRequest): String? {
        val xForwardedFor = request.getHeader("X-Forwarded-For")
        return if (!xForwardedFor.isNullOrBlank()) {
            xForwardedFor.split(",").firstOrNull()?.trim()
        } else {
            request.remoteAddr
        }
    }
}
