package fr.gouv.dgampa.rapportnav.config

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.use_cases.apikey.ApiKeyService
import fr.gouv.dgampa.rapportnav.domain.use_cases.apikey.RateLimitException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.time.Instant

@Component
class ApiKeyAuthenticationFilter(
    private val apiKeyService: ApiKeyService,
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {

    private val logger = LoggerFactory.getLogger(ApiKeyAuthenticationFilter::class.java)

    companion object {
        const val API_KEY_HEADER = "X-API-Key"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val clientIp = getClientIp(request)
        val apiKey = request.getHeader(API_KEY_HEADER)?.trim()

        if (apiKey != null ) {
            try {
                val validatedKey = apiKeyService.validateApiKey(
                    apiKey = apiKey,
                    ipAddress = clientIp,
                    requestPath = request.requestURI
                )

                val authorities = listOf(SimpleGrantedAuthority("ROLE_API_USER"))

                val authentication = UsernamePasswordAuthenticationToken(
                    validatedKey?.owner ?: "unknown",
                    null,
                    authorities
                )

                authentication.details = mapOf(
                    "apiKeyId" to validatedKey?.id,
                    "publicId" to validatedKey?.publicId,
                    "ipAddress" to clientIp
                )

                SecurityContextHolder.getContext().authentication = authentication

            } catch (e: RateLimitException) {
                logger.warn("Rate limit exceeded: ${e.message}")
                respondWithError(response, HttpStatus.TOO_MANY_REQUESTS, e.message ?: "Rate limit exceeded")
                return
            } catch (e: Exception) {
                logger.error("Error during API key validation", e)
                respondWithError(response, HttpStatus.UNAUTHORIZED, "Authentication failed")
                return
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun getClientIp(request: HttpServletRequest): String {
        val headers = listOf(
            "X-Forwarded-For",
            "X-Real-IP",
            "Proxy-Client-IP"
        )

        for (header in headers) {
            val ip = request.getHeader(header)
            if (!ip.isNullOrBlank() && ip != "unknown") {
                return ip.split(",")[0].trim()
            }
        }

        return request.remoteAddr ?: "unknown"
    }

    private fun respondWithError(response: HttpServletResponse, status: HttpStatus, message: String) {
        response.status = status.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        val errorResponse = mapOf(
            "error" to status.reasonPhrase,
            "message" to message,
            "timestamp" to Instant.now().toString()
        )

        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }
}
