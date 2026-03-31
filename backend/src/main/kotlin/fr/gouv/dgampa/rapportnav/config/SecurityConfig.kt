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
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter

/**
 * This class sets all security related configuration.
 */
@Configuration
@EnableWebSecurity(debug = false)
class SecurityConfig(
    private val customAuthenticationFilter: CustomAuthenticationFilter,
    private val apiKeyAuthFilter: ApiKeyAuthenticationFilter,
    private val sentryUserContextFilter: SentryUserContextFilter
) {

    /**
     * Security filter for API key authenticated endpoints (backend-to-backend calls).
     * CSRF is disabled because:
     * 1. API keys are sent via headers, not cookies
     * 2. This is server-to-server communication, not browser-based
     * 3. Session is stateless (no cookies to exploit)
     */
    @Bean
    @Order(1)
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

        // CSRF protection is intentionally disabled for the following reasons:
        // 1. This API uses stateless JWT Bearer token authentication
        // 2. Tokens are sent via Authorization header, not cookies
        // 3. Browsers don't automatically include Authorization headers in cross-origin requests
        // 4. Session management is STATELESS (no JSESSIONID cookies)
        // See: https://security.stackexchange.com/questions/170388/
        http.csrf { csrf ->
            csrf.disable()
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

        // Add security headers
        // Note: FrontendRoutesController also sets CSP with nonce for HTML responses
        http.headers { headers ->
            headers
                .frameOptions { frame ->
                    frame.deny() // prevent clickjacking
                }
                .contentTypeOptions { } // X-Content-Type-Options: nosniff - prevents MIME-sniffing attacks
                .httpStrictTransportSecurity { hsts ->
                    hsts
                        .includeSubDomains(true)
                        .maxAgeInSeconds(63072000) // 2 years (OWASP recommendation)
                }
                .referrerPolicy { referrer ->
                    // set strict origin policy
                    referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                }
                .permissionsPolicyHeader { permissions ->
                    // security in depth - reduce attack surface
                    // prevent xss via these APIs
                    permissions.policy("geolocation=(), camera=(), microphone=()")
                }
                .contentSecurityPolicy { csp ->
                    // CSP for API responses (defense in depth)
                    // Note: HTML responses get a more specific CSP with nonce from FrontendRoutesController
                    val upgradeInsecure = if (System.getenv("ENV_PROFILE") == "prod") "; upgrade-insecure-requests" else ""
                    csp.policyDirectives(
                        "default-src 'self'; " +
                        "script-src 'self'; " +
                        "connect-src 'self' https://sentry.incubateur.net https://recherche-entreprises.api.gouv.fr https://data.geopf.fr; " +
                        "frame-ancestors 'none'" +
                        upgradeInsecure
                    )
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
