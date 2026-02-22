package fr.gouv.dgampa.rapportnav.config

import fr.gouv.dgampa.rapportnav.domain.entities.user.AuthoritiesEnum
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository

/**
 * This class sets all security related configuration.
 */
@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val customAuthenticationFilter: CustomAuthenticationFilter,
    private val apiKeyAuthFilter: ApiKeyAuthenticationFilter,
    private val sentryUserContextFilter: SentryUserContextFilter
) {

    @Bean
    @Order(1)
    // api-key filter
    fun apiKeySecurityFilter(http: HttpSecurity): SecurityFilterChain {
        http
            .securityMatcher("/api/analytics/**")
            .addFilterBefore(apiKeyAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
            .addFilterAfter(sentryUserContextFilter, ApiKeyAuthenticationFilter::class.java)
            .authorizeHttpRequests { it.anyRequest().hasAuthority(AuthoritiesEnum.ROLE_API_USER.toString()) }
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }

        return http.build()
    }

    @Bean
    @Order(2)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {

        // Add custom authentication filter
        http.addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        // Add Sentry user context filter after authentication
        http.addFilterAfter(sentryUserContextFilter, CustomAuthenticationFilter::class.java)

        // cors
        http.cors(Customizer.withDefaults())

        // session
        http.sessionManagement { sessionManagement ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }

//        val requestHandler = CsrfTokenRequestAttributeHandler()
//        requestHandler.setCsrfRequestAttributeName(null)
        val csrfTokenRequestHandler = SpaCsrfTokenRequestHandler()

        // Configure the CSRF token repository with cookie customization
        val csrfTokenRepository = CookieCsrfTokenRepository
            .withHttpOnlyFalse() // keep token accessible from js

        // Apply CSRF configuration
        http.csrf { csrf ->
            csrf.disable()
//            csrf.csrfTokenRepository(csrfTokenRepository)
//                .csrfTokenRequestHandler(csrfTokenRequestHandler)
            //            .apply {
//                setCookieCustomizer { cookie ->
//                    cookie.path("/")  // Makes the cookie available for all paths
//                    cookie.secure(true) // Use secure cookies for all
//                    cookie.maxAge(-1) // Default: Session cookie
//                    cookie.sameSite(SameSite.LAX.name) // SameSite policy: Lax
//                }
//            }
        }

        // route authorizations - Updated for Spring Boot 3.5.0
        http.authorizeHttpRequests { authorize ->
            enableSwagger(authorize)
                .requestMatchers("/api/v1/auth/login").permitAll()
                .requestMatchers("/api/v1/auth/register").hasAuthority(AuthoritiesEnum.ROLE_ADMIN.toString())
                .requestMatchers("/api/v2/admin/**").hasAuthority(AuthoritiesEnum.ROLE_ADMIN.toString())
                .requestMatchers("/api/v2/**").authenticated()
                .requestMatchers("/**").permitAll()
                .anyRequest().authenticated()
        }

        // Add security headers (CSP and frame options)
        http.headers { headers ->
            headers
                .contentSecurityPolicy { csp ->
                    csp.policyDirectives(
                        "default-src 'self'; " +
                            "script-src 'self'; " +
                            "style-src 'self' 'unsafe-inline'; " +
                            "style-src-elem 'self' 'unsafe-inline'; " +
                            "img-src 'self'; " +
                            "font-src 'self' data:; " +
                            "connect-src 'self' https://sentry.incubateur.net https://recherche-entreprises.api.gouv.fr; " +
                            "frame-src 'none'; " +
                            "base-uri 'self'; " +
                            "frame-ancestors 'none'; " +
                            "form-action 'self'; " +
                            "object-src 'none'; " +
                            "upgrade-insecure-requests;"
                    )
                }
                .frameOptions { frame ->
                    frame.deny() // Prevent clickjacking
                }
        }

        // allow anon users
        http.anonymous(Customizer.withDefaults())

        return http.build()
    }

    private fun enableSwagger(authorize: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry): AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry {
        val env = System.getenv("ENV_PROFILE")
        if (env == "prod") return authorize
        authorize
            .requestMatchers(
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/v3/api-docs/**"
            ).permitAll()
        return authorize
    }
}
