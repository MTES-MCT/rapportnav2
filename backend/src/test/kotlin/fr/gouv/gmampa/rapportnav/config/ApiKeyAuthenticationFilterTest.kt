package fr.gouv.gmampa.rapportnav.config

import fr.gouv.dgampa.rapportnav.config.ApiKeyAuthenticationFilter
import fr.gouv.dgampa.rapportnav.domain.entities.apikey.ApiKeyEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.apikey.RateLimitException
import fr.gouv.dgampa.rapportnav.domain.use_cases.apikey.ValidateApiKey
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import tools.jackson.databind.json.JsonMapper
import java.util.*

class ApiKeyAuthenticationFilterTest {
    private lateinit var validateApiKey: ValidateApiKey
    private lateinit var objectMapper: JsonMapper
    private lateinit var filter: ApiKeyAuthenticationFilter
    private lateinit var request: HttpServletRequest
    private lateinit var response: MockHttpServletResponse
    private lateinit var filterChain: FilterChain

    @BeforeEach
    fun setUp() {
        validateApiKey = mock()
        objectMapper = JsonMapper()
        filter = ApiKeyAuthenticationFilter(validateApiKey, objectMapper)
        request = mock()
        response = MockHttpServletResponse()
        filterChain = mock()
        SecurityContextHolder.clearContext()
    }

    @Test
    fun `should authenticate valid API key`() {
        val apiKeyEntity = ApiKeyEntity(
            id = UUID.randomUUID(),
            publicId = "abcd1234",
            hashedKey = "hashed",
            owner = "testUser"
        )

        whenever(request.getHeader("X-API-Key")).thenReturn("abcd1234efgh")
        whenever(request.requestURI).thenReturn("/api/test")
        whenever(request.remoteAddr).thenReturn("1.2.3.4")
        whenever(validateApiKey.execute(any(), any(), any())).thenReturn(apiKeyEntity)

        filter.doFilterInternal(request, response, filterChain)

        val auth = SecurityContextHolder.getContext().authentication
        assertNotNull(auth)
        assertEquals("testUser", auth?.name)
        verify(filterChain).doFilter(request, response)
    }

    @Test
    fun `should proceed without authentication when header missing`() {
        whenever(request.getHeader("X-API-Key")).thenReturn(null)

        filter.doFilterInternal(request, response, filterChain)

        val auth = SecurityContextHolder.getContext().authentication
        assertNull(auth)
        verify(filterChain).doFilter(request, response)
    }

    @Test
    fun `should return 401 when validateApiKey throws exception`() {
        whenever(request.getHeader("X-API-Key")).thenReturn("abcd1234efgh")
        whenever(request.requestURI).thenReturn("/api/test")
        whenever(request.remoteAddr).thenReturn("1.2.3.4")
        whenever(validateApiKey.execute(any(), any(), any())).thenThrow(RuntimeException("bad"))

        filter.doFilterInternal(request, response, filterChain)

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.status)
    }

    @Test
    fun `should extract client IP from X-Forwarded-For`() {
        whenever(request.getHeader("X-Forwarded-For")).thenReturn("5.6.7.8")
        whenever(request.getHeader("X-API-Key")).thenReturn(null)

        filter.doFilterInternal(request, response, filterChain)

        verify(filterChain).doFilter(request, response)
    }

    @Test
    fun `should return 429 when validateApiKey throws RateLimitException`() {
        whenever(request.getHeader("X-API-Key")).thenReturn("abcd1234efgh")
        whenever(request.requestURI).thenReturn("/api/test")
        whenever(request.remoteAddr).thenReturn("1.2.3.4")
        whenever(validateApiKey.execute(any(), any(), any())).thenThrow(RateLimitException("limit exceeded"))

        filter.doFilterInternal(request, response, filterChain)

        assertEquals(HttpStatus.TOO_MANY_REQUESTS.value(), response.status)
    }
}
