package fr.gouv.dgampa.rapportnav.config


import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler
import org.springframework.security.web.util.matcher.AntPathRequestMatcher


/**
 * This class sets all security related configuration.
 */
@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val tokenService: TokenService,
) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        // Configure JWT
        http.oauth2ResourceServer().jwt()
        http.authenticationManager { auth ->
            val jwt = auth as BearerTokenAuthenticationToken
            val user = tokenService.parseToken(jwt.token) ?: throw InvalidBearerTokenException("Invalid token")
            val grantedAuthorities = user.roles.map { role -> SimpleGrantedAuthority("ROLE_$role") }
            UsernamePasswordAuthenticationToken(user, "", grantedAuthorities)
        }

        // Other configuration
        http.cors()
        http.sessionManagement { sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        val requestHandler = CsrfTokenRequestAttributeHandler()
        requestHandler.setCsrfRequestAttributeName(null)
        http.csrf { csrf ->
            csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .csrfTokenRequestHandler(requestHandler)
        }
        http.authorizeHttpRequests { authorize ->
            authorize
                .requestMatchers(AntPathRequestMatcher("/graphql")).authenticated()
                .requestMatchers(AntPathRequestMatcher("/api/v1/auth/login")).permitAll()
                .requestMatchers(AntPathRequestMatcher("/api/v1/auth/register")).permitAll()
                .requestMatchers(AntPathRequestMatcher("/api/v1/admin/**")).hasRole("ADMIN")
                .requestMatchers(AntPathRequestMatcher("/**")).permitAll()
                .anyRequest().authenticated()
        }

        return http.build()
    }

}
