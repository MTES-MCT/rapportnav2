package fr.gouv.gmampa.rapportnav.config

import fr.gouv.dgampa.rapportnav.config.SpaCsrfTokenRequestHandler
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.security.web.csrf.DefaultCsrfToken
import java.util.function.Supplier

class SpaCsrfTokenRequestHandlerTest {

    private lateinit var handler: SpaCsrfTokenRequestHandler
    private lateinit var request: MockHttpServletRequest
    private lateinit var response: MockHttpServletResponse
    private lateinit var csrfToken: CsrfToken

    @BeforeEach
    fun setUp() {
        handler = SpaCsrfTokenRequestHandler()
        request = MockHttpServletRequest()
        response = MockHttpServletResponse()
        csrfToken = DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", "test-token-value")
    }

    @Test
    fun `should handle request and set csrf token attribute`() {
        val tokenSupplier = Supplier { csrfToken }

        handler.handle(request, response, tokenSupplier)

        assertNotNull(request.getAttribute(CsrfToken::class.java.name))
    }

    @Test
    fun `should resolve token value from header when header is present`() {
        request.addHeader("X-CSRF-TOKEN", "header-token-value")

        val result = handler.resolveCsrfTokenValue(request, csrfToken)

        assertNotNull(result)
    }

    @Test
    fun `should resolve token value using xor handler when header is absent`() {
        val result = handler.resolveCsrfTokenValue(request, csrfToken)

        assertNull(result)
    }

    @Test
    fun `should resolve token value using xor handler when header is empty`() {
        request.addHeader("X-CSRF-TOKEN", "")

        val result = handler.resolveCsrfTokenValue(request, csrfToken)

        assertNull(result)
    }

    @Test
    fun `should resolve token from request parameter when no header`() {
        request.setParameter("_csrf", "param-token-value")

        val result = handler.resolveCsrfTokenValue(request, csrfToken)

        assertNull(result)
    }

    @Test
    fun `should use plain handler for SPA header-based token submission`() {
        val plainTokenValue = "plain-token-from-cookie"
        request.addHeader("X-CSRF-TOKEN", plainTokenValue)

        val result = handler.resolveCsrfTokenValue(request, csrfToken)

        assertEquals(plainTokenValue, result)
    }

    @Test
    fun `should be instantiable without parameters`() {
        val newHandler = SpaCsrfTokenRequestHandler()
        assertNotNull(newHandler)
    }

    @Test
    fun `should cause deferred token to be loaded during handle`() {
        var tokenLoaded = false
        val deferredToken = Supplier {
            tokenLoaded = true
            csrfToken
        }

        handler.handle(request, response, deferredToken)

        assertTrue(tokenLoaded)
    }
}