package fr.gouv.dgampa.rapportnav.config

import fr.gouv.dgampa.rapportnav.infrastructure.api.filter.ApiKeyAuthenticationFilter
import fr.gouv.dgampa.rapportnav.infrastructure.api.filter.CustomAuthenticationFilter
import fr.gouv.dgampa.rapportnav.infrastructure.api.filter.ProConnectUserBridgeFilter
import fr.gouv.dgampa.rapportnav.infrastructure.api.filter.SentryUserContextFilter
import fr.gouv.dgampa.rapportnav.domain.entities.user.AuthoritiesEnum
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter

/**
 * This class sets all security related configuration.
 */
@Configuration
@EnableWebSecurity(debug = false)
@EnableMethodSecurity
class SecurityConfig(
    private val customAuthenticationFilter: CustomAuthenticationFilter,
    private val apiKeyAuthFilter: ApiKeyAuthenticationFilter,
    private val sentryUserContextFilter: SentryUserContextFilter,
    private val oidcProperties: OIDCProperties,
    // The following ProConnect beans only exist when `rapportnav.oidc.enabled=true`.
    private val clientRegistrationRepository: ClientRegistrationRepository?,
    private val proConnectOidcUserService: ProConnectOidcUserService?,
    private val proConnectUserBridgeFilter: ProConnectUserBridgeFilter?,
    @Value("\${metabase.site-url}") private val metabaseSiteUrl: String
) {
    private val logger = LoggerFactory.getLogger(SecurityConfig::class.java)

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
            .securityMatcher("/api/analytics/**", "/api/v1/public/inquiries", "/api/v1/public/inquiries/**")
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

        // Add custom authentication filter (legacy email/password + JWT Bearer path)
        http.addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        // Bridge a ProConnect (OIDC) session principal into the domain User principal.
        // Runs after the JWT filter so a Bearer token still takes precedence when present.
        proConnectUserBridgeFilter?.let {
            http.addFilterAfter(it, CustomAuthenticationFilter::class.java)
        }
        // Add Sentry user context filter after authentication
        http.addFilterAfter(sentryUserContextFilter, CustomAuthenticationFilter::class.java)

        // cors
        http.cors(Customizer.withDefaults())

        // session
        // When ProConnect is enabled we need IF_REQUIRED so Spring can persist the OAuth2
        // authorization request and the authenticated session. Bearer/password requests never
        // touch the session, so they stay effectively stateless. When ProConnect is disabled
        // the app remains fully STATELESS exactly as before.
        http.sessionManagement { sessionManagement ->
            sessionManagement.sessionCreationPolicy(
                if (oidcProperties.enabled) SessionCreationPolicy.IF_REQUIRED else SessionCreationPolicy.STATELESS
            )
        }

        // CSRF protection is intentionally disabled for the following reasons:
        // 1. The legacy path uses stateless JWT Bearer token authentication (header, not cookie)
        // 2. Browsers don't automatically include Authorization headers in cross-origin requests
        // 3. The ProConnect session cookie is mitigated via SameSite=Lax (see application.properties)
        //    and mirrors MonitorEnv's configuration.
        http.csrf { csrf ->
            csrf.disable()
        }

        // route authorizations - Updated for Spring Boot 3.5.0
        http.authorizeHttpRequests { authorize ->
            enableSwagger(authorize)
                .requestMatchers("/api/v1/auth/login").permitAll()
                // ProConnect (OIDC) entry, callback and logout endpoints
                .requestMatchers("/oauth2/**", "/login/oauth2/**", "/logout").permitAll()
                .requestMatchers("/api/v1/auth/register").hasAuthority(AuthoritiesEnum.ROLE_ADMIN.toString())
                .requestMatchers("/api/v2/admin/**").hasAuthority(AuthoritiesEnum.ROLE_ADMIN.toString())
                .requestMatchers("/api/v2/manage/**")
                .hasAnyAuthority(
                    AuthoritiesEnum.ROLE_ADMIN.toString(),
                    AuthoritiesEnum.ROLE_MANAGER_PAM.toString(),
                    AuthoritiesEnum.ROLE_MANAGER_ULAM.toString()
                )
                .requestMatchers("/api/v2/**").authenticated()
                .requestMatchers("/**").permitAll()
                .anyRequest().authenticated()
        }

        // ProConnect (OIDC) login — only wired when enabled and a client registration exists.
        if (oidcProperties.enabled && clientRegistrationRepository != null && proConnectOidcUserService != null) {
            logger.info("✅ ProConnect (OIDC) authentication is ENABLED.")
            http.oauth2Login { oauth2 ->
                oauth2
                    .loginPage("/login")
                    .userInfoEndpoint { userInfo -> userInfo.oidcUserService(proConnectOidcUserService) }
                    .successHandler(SimpleUrlAuthenticationSuccessHandler(oidcProperties.successUrl))
                    .failureHandler(SimpleUrlAuthenticationFailureHandler(oidcProperties.errorUrl))
                // When 2FA is enforced, inject the OIDC `claims` parameter so ProConnect performs
                // (and asserts) multi-factor authentication. Left untouched otherwise.
                if (oidcProperties.forceMfa) {
                    logger.info("✅ ProConnect 2FA (MFA) enforcement is ENABLED.")
                    oauth2.authorizationEndpoint { endpoint ->
                        endpoint.authorizationRequestResolver(
                            mfaAuthorizationRequestResolver(clientRegistrationRepository),
                        )
                    }
                }
            }
            http.logout { logout ->
                logout
                    .logoutUrl("/logout")
                    .logoutSuccessHandler(oidcLogoutSuccessHandler(clientRegistrationRepository))
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .deleteCookies("JSESSIONID")
            }
            // Return 401 (not a redirect to ProConnect) when an XHR hits a protected /api resource
            // unauthenticated, so the SPA can react instead of following a 302 to the IdP.
            http.exceptionHandling { it.authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) }
        } else {
            logger.warn("⚠️  ProConnect (OIDC) authentication is DISABLED — email/password login only.")
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
                // CSP for API / non-HTML responses (defense in depth). HTML responses get a more
                // specific CSP with a nonce from FrontendRoutesController; because Spring's CSP writer
                // only sets the header when absent (containsHeader guard), that richer policy wins for
                // HTML and this one applies to everything else.
                //
                // We emit this per-request (not via the static .contentSecurityPolicy DSL) so that
                // `upgrade-insecure-requests` keys off request.isSecure — the SAME signal the HTML CSP
                // uses — instead of ENV_PROFILE. This keeps the two policies aligned on every env
                // (HTTP integration: omitted; HTTPS prod: added) with a single source of truth.
                .addHeaderWriter { request, response ->
                    if (!response.containsHeader("Content-Security-Policy")) {
                        val upgradeInsecure = if (request.isSecure) "; upgrade-insecure-requests" else ""
                        response.setHeader(
                            "Content-Security-Policy",
                            "default-src 'self'; " +
                            "script-src 'self'; " +
                            "connect-src 'self' https://sentry.incubateur.net https://recherche-entreprises.api.gouv.fr https://data.geopf.fr; " +
                            "frame-src $metabaseSiteUrl; " +
                            "frame-ancestors 'none'" +
                            upgradeInsecure
                        )
                    }
                }
        }

        // allow anon users
        http.anonymous(Customizer.withDefaults())

        return http.build()
    }

    /**
     * Builds an [OAuth2AuthorizationRequestResolver] that adds the OIDC `claims` parameter to the
     * ProConnect `/authorize` request, marking `acr` as an *essential* claim with the accepted MFA
     * levels. ProConnect requires the `claims` parameter (not `acr_values`, which is only a
     * preference) to actually force two-factor authentication. Spring URL-encodes additional
     * parameters when it builds the redirect URI.
     */
    private fun mfaAuthorizationRequestResolver(
        clientRegistrationRepository: ClientRegistrationRepository,
    ): OAuth2AuthorizationRequestResolver {
        val acrValuesJson = oidcProperties.mfaAcrValues.joinToString(",") { "\"$it\"" }
        val claimsJson = """{"id_token":{"acr":{"essential":true,"values":[$acrValuesJson]}}}"""
        val resolver = DefaultOAuth2AuthorizationRequestResolver(
            clientRegistrationRepository,
            "/oauth2/authorization",
        )
        resolver.setAuthorizationRequestCustomizer { builder ->
            builder.additionalParameters { params -> params["claims"] = claimsJson }
        }
        return resolver
    }

    /**
     * Logs the ProConnect session out both locally and at the ProConnect IdP (end-session),
     * then returns the browser to the SPA login page.
     */
    private fun oidcLogoutSuccessHandler(
        clientRegistrationRepository: ClientRegistrationRepository,
    ): LogoutSuccessHandler {
        val handler = OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository)
        handler.setPostLogoutRedirectUri(oidcProperties.postLogoutRedirectUri)
        // Diagnostic wrapper: logs the principal present at logout. If it's not an OidcUser,
        // the handler falls back to redirecting to "/" (never hitting ProConnect end-session),
        // which is the "lands on http://localhost/" symptom.
        return LogoutSuccessHandler { request, response, authentication ->
            logger.info(
                "OIDC logout - authentication={}, principalType={}",
                authentication,
                authentication?.principal?.javaClass?.name,
            )
            handler.onLogoutSuccess(request, response, authentication)
        }
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
