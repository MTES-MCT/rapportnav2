package fr.gouv.dgampa.rapportnav.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
class CorsConfig {
    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()

        // Allow specific origins, methods, and headers
        config.addAllowedOrigin("*") // Replace with specific origins as needed
        config.addAllowedMethod("*") // Allow all HTTP methods
        config.addAllowedHeader("*") // Allow all headers
//        config.allowCredentials = true // Allow credentials, if needed

        // Register CORS configuration for specific paths
        source.registerCorsConfiguration("/**", config)

        return CorsFilter(source)
    }
}
