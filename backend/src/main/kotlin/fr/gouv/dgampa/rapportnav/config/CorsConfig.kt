package fr.gouv.dgampa.rapportnav.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class CorsConfig(
    // SpEL expression to properly split comma-separated origins into a List
    @Value("#{\"\${cors.allowed-origins}\".split(',')}")
    private val allowedOrigins: List<String>
) {
    /**
     * Provides CORS configuration for Spring Security.
     * Using CorsConfigurationSource (instead of CorsFilter) integrates properly
     * with Spring Security's http.cors() configuration.
     */
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val config = CorsConfiguration()

        // Allow only configured origins (environment-specific)
        allowedOrigins.forEach { config.addAllowedOrigin(it.trim()) }

        // Restrict to necessary HTTP methods
        config.addAllowedMethod("GET")
        config.addAllowedMethod("POST")
        config.addAllowedMethod("PUT")
        config.addAllowedMethod("PATCH")
        config.addAllowedMethod("DELETE")
        config.addAllowedMethod("OPTIONS")

        // Restrict to necessary headers
        config.addAllowedHeader("Authorization")
        config.addAllowedHeader("Content-Type")
        config.addAllowedHeader("X-Requested-With")
        config.addAllowedHeader("Accept")

        config.allowCredentials = false

        // Cache preflight response for 1 hour
        config.maxAge = 3600L

        // Register CORS configuration for API paths only
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/api/**", config)

        return source
    }
}
