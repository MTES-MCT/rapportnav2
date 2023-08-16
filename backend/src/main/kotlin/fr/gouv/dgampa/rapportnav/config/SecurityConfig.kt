package fr.gouv.dgampa.rapportnav.config


import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

/**
 * This class sets all security related configuration.
 */
@Configuration
@EnableWebSecurity
class SecurityConfig (
    private val tokenService: TokenService,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        // Define public and private routes
        // http.authorizeHttpRequests()
//            .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
//            .requestMatchers(HttpMethod.POST, "/api/v1/auth/register").permitAll()
////            .requestMatchers("/api/**").authenticated()
//            .anyRequest().permitAll()

// http.authorizeHttpRequests()
            // .requestMatchers(
            //     "/",
            //     "/index.html",
            //     "/*.js",
            //     "/*.png",
            //     "/*.svg",
            //     "/static/**",
            //     "/map-icons/**",
            //     "/flags/**",
            //     "/robots.txt",
            //     "/favicon-32.ico",
            //     "/asset-manifest.json",
            //     "/swagger-ui/**",
            //     // Used to redirect to the frontend SPA, see SpaController.kt
            //     "/error",
            //     "/api/v1/auth/**",
            //     "/version",
            //     "/login",
            // ).permitAll()
            // .anyRequest().authenticated()


        // Configure JWT
        http.oauth2ResourceServer().jwt()
        http.authenticationManager { auth ->
            val jwt = auth as BearerTokenAuthenticationToken
            val user = tokenService.parseToken(jwt.token) ?: throw InvalidBearerTokenException("Invalid token")
            UsernamePasswordAuthenticationToken(user, "", listOf(SimpleGrantedAuthority("USER")))
        }

        // Other configuration
        http.cors()
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http.csrf().disable()
        http.headers().frameOptions().disable()
        http.headers().xssProtection().disable()
        http.authorizeHttpRequests { authorize -> authorize.requestMatchers(AntPathRequestMatcher("/**")).permitAll() }

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        // allow localhost for dev purposes
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("http://localhost:3000", "http://localhost:8080")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
        configuration.allowedHeaders = listOf("authorization", "content-type")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}
