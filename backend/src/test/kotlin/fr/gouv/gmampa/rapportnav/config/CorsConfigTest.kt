package fr.gouv.gmampa.rapportnav.config

import fr.gouv.dgampa.rapportnav.config.CorsConfig
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

class CorsConfigTest {

    @Test
    fun `should create CorsConfigurationSource bean with allowed origins`() {
        val allowedOrigins = listOf("http://localhost:3000", "http://localhost:5173")
        val config = CorsConfig(allowedOrigins)

        val source = config.corsConfigurationSource()

        assertNotNull(source)
        assertTrue(source is CorsConfigurationSource)
    }

    @Test
    fun `should create CorsConfigurationSource bean with single origin`() {
        val allowedOrigins = listOf("https://rapport-nav.din.developpement-durable.gouv.fr")
        val config = CorsConfig(allowedOrigins)

        val source = config.corsConfigurationSource()

        assertNotNull(source)
        assertTrue(source is UrlBasedCorsConfigurationSource)
    }

    @Test
    fun `should configure CORS for api paths`() {
        val allowedOrigins = listOf("http://localhost:3000")
        val config = CorsConfig(allowedOrigins)

        val source = config.corsConfigurationSource()

        val request = MockHttpServletRequest("GET", "/api/v2/missions")
        val corsConfig = source.getCorsConfiguration(request)

        assertNotNull(corsConfig)
        assertTrue(corsConfig!!.allowedOrigins!!.contains("http://localhost:3000"))
        assertTrue(corsConfig.allowedMethods!!.containsAll(listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")))
    }

    @Test
    fun `should not configure CORS for non-api paths`() {
        val allowedOrigins = listOf("http://localhost:3000")
        val config = CorsConfig(allowedOrigins)

        val source = config.corsConfigurationSource()

        val request = MockHttpServletRequest("GET", "/swagger-ui/index.html")
        val corsConfig = source.getCorsConfiguration(request)

        assertNull(corsConfig)
    }
}
