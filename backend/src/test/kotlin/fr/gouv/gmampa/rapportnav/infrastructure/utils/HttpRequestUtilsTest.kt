package fr.gouv.gmampa.rapportnav.infrastructure.utils

import fr.gouv.dgampa.rapportnav.infrastructure.utils.HttpRequestUtils
import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class HttpRequestUtilsTest {

    @Test
    fun `should return X-Real-IP when present`() {
        val request: HttpServletRequest = mock()
        whenever(request.getHeader("X-Real-IP")).thenReturn("  1.2.3.4  ")
        whenever(request.remoteAddr).thenReturn("10.0.0.1")

        val result = HttpRequestUtils.getClientIp(request)

        assertEquals("1.2.3.4", result)
    }

    @Test
    fun `should return remoteAddr when X-Real-IP is null`() {
        val request: HttpServletRequest = mock()
        whenever(request.getHeader("X-Real-IP")).thenReturn(null)
        whenever(request.remoteAddr).thenReturn("10.0.0.1")

        val result = HttpRequestUtils.getClientIp(request)

        assertEquals("10.0.0.1", result)
    }

    @Test
    fun `should return remoteAddr when X-Real-IP is blank`() {
        val request: HttpServletRequest = mock()
        whenever(request.getHeader("X-Real-IP")).thenReturn("   ")
        whenever(request.remoteAddr).thenReturn("10.0.0.1")

        val result = HttpRequestUtils.getClientIp(request)

        assertEquals("10.0.0.1", result)
    }

    @Test
    fun `should return unknown when both X-Real-IP and remoteAddr are null`() {
        val request: HttpServletRequest = mock()
        whenever(request.getHeader("X-Real-IP")).thenReturn(null)
        whenever(request.remoteAddr).thenReturn(null)

        val result = HttpRequestUtils.getClientIp(request)

        assertEquals("unknown", result)
    }

    @Test
    fun `should return User-Agent header when present`() {
        val request: HttpServletRequest = mock()
        whenever(request.getHeader("User-Agent")).thenReturn("Mozilla/5.0")

        val result = HttpRequestUtils.getUserAgent(request)

        assertEquals("Mozilla/5.0", result)
    }

    @Test
    fun `should return null when User-Agent header is missing`() {
        val request: HttpServletRequest = mock()
        whenever(request.getHeader("User-Agent")).thenReturn(null)

        val result = HttpRequestUtils.getUserAgent(request)

        assertEquals(null, result)
    }
}
