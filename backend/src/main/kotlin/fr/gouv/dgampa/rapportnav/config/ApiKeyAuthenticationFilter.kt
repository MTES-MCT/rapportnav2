package fr.gouv.dgampa.rapportnav.config

import fr.gouv.dgampa.rapportnav.domain.use_cases.apikey.RateLimitException
import fr.gouv.dgampa.rapportnav.domain.use_cases.apikey.ValidateApiKey
import fr.gouv.dgampa.rapportnav.infrastructure.utils.HttpRequestUtils
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
import tools.jackson.databind.json.JsonMapper
import java.time.Instant

@Component
class ApiKeyAuthenticationFilter(
    private val validateApiKey: ValidateApiKey,
    private val objectMapper: JsonMapper
) : OncePerRequestFilter() {

    private val logger = LoggerFactory.getLogger(ApiKeyAuthenticationFilter::class.java)

    companion object {
        const val API_KEY_HEADER = "X-API-Key"
    }

    public override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val clientIp = HttpRequestUtils.getClientIp(request)
        val apiKey = request.getHeader(API_KEY_HEADER)?.trim()

        if (apiKey != null) {
            try {
                val validatedKey = validateApiKey.execute(
                    apiKey = apiKey,
                    ipAddress = clientIp,
                    requestPath = request.requestURI,
                )

                if (validatedKey != null) {
                    val authorities = listOf(SimpleGrantedAuthority("ROLE_API_USER"))
                    val authentication = UsernamePasswordAuthenticationToken(
                        validatedKey.owner ?: "unknown",
                        null,
                        authorities
                    )
                    authentication.details = mapOf(
                        "apiKeyId" to validatedKey.id,
                        "publicId" to validatedKey.publicId,
                        "ipAddress" to clientIp
                    )

                    SecurityContextHolder.getContext().authentication = authentication
                    logger.debug("API key authenticated: publicId=${validatedKey.publicId}")
                }

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
