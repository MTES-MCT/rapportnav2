package fr.gouv.dgampa.rapportnav.config


import fr.gouv.dgampa.rapportnav.domain.entities.user.AuthoritiesEnum
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

/**
 * This class sets all security related configuration.
 */
@Configuration
@EnableWebSecurity(debug=true)
class SecurityConfig(
    private val tokenService: TokenService,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {

        // configure JWT token
        http.oauth2ResourceServer { oauth2 ->
            oauth2.jwt(Customizer.withDefaults())
        }

        // update authenticationManager and security context with authorities from roles
        http.authenticationManager { auth ->
            val jwt = auth as BearerTokenAuthenticationToken
            val user = tokenService.parseToken(jwt.token) ?: throw InvalidBearerTokenException("Invalid token")

            println("Authentication Manager - User: $user")
            println("Authentication Manager - Roles: ${user.roles}")

            val grantedAuthorities = user.roles.map {
                    role -> SimpleGrantedAuthority("ROLE_$role")
            }

            val authentication = UsernamePasswordAuthenticationToken(user, "", grantedAuthorities)
            println("AuthenticationManager - SecurityContext: ${SecurityContextHolder.getContext().authentication}")
            println("Authentication Manager - Granted Authorities: $grantedAuthorities")

            authentication
        }

        // cors
        http.cors(Customizer.withDefaults())

        // session
        http.sessionManagement { sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }

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

        // route authorizations
        http.authorizeHttpRequests { authorize ->
            authorize
                .requestMatchers(AntPathRequestMatcher("/graphql")).authenticated()
                .requestMatchers(AntPathRequestMatcher("/api/v1/auth/login")).permitAll()
                .requestMatchers(AntPathRequestMatcher("/api/v1/auth/register")).authenticated()
                .requestMatchers(AntPathRequestMatcher("/api/v1/auth/register")).hasAuthority(AuthoritiesEnum.ROLE_ADMIN.toString())
                .requestMatchers(AntPathRequestMatcher("/api/v1/admin/**")).hasAuthority(AuthoritiesEnum.ROLE_ADMIN.toString())
                .requestMatchers(AntPathRequestMatcher("/api/v2/**")).authenticated()
                .requestMatchers(AntPathRequestMatcher("/**")).permitAll()
                .anyRequest().authenticated()
        }

        // Add security headers (CSP and frame options)
        http.headers { headers ->
            headers
                .contentSecurityPolicy { csp ->
                    // Updated CSP to allow base64 fonts
                    csp.policyDirectives(
                        "default-src 'self'; " +
                            "script-src 'self' 'unsafe-inline' https://github.com; " +
                            "style-src 'self' 'unsafe-inline'; " +
                            "style-src-elem 'self' 'unsafe-inline'; " +
                            "img-src 'self'; " +
                            "font-src 'self' data:; " + // Allow base64-encoded fonts
                            "connect-src 'self' https://sentry.incubateur.net; " +
                            "frame-src 'self'; " +
                            "base-uri 'self'; " +
                            "frame-ancestors 'none'; " +
                            "form-action 'self'; " +
                            "object-src 'none'; " +
                            "require-trusted-types-for 'script';"
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

}
