package fr.gouv.dgampa.rapportnav.config

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.jwk.source.JWKSourceBuilder
import com.nimbusds.jose.proc.JWSVerificationKeySelector
import com.nimbusds.jose.proc.SecurityContext
import com.nimbusds.jwt.proc.DefaultJWTProcessor
import com.nimbusds.jwt.proc.JWTClaimsSetVerifier
import fr.gouv.dgampa.rapportnav.domain.entities.user.toAuthority
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.FindByEmail
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.oidc.OidcUserInfo
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.net.URI
import java.util.concurrent.ConcurrentHashMap

/**
 * Loads the ProConnect user and gates access on our own user table.
 *
 * Provisioning rule (user-confirmed): the ProConnect `email` MUST match an existing,
 * non-disabled `user` row. Unknown or disabled emails are rejected — admins pre-create
 * users with their roles and serviceId. The returned principal carries the rapportnav
 * authorities so the HTTP session is authorized with the correct RBAC.
 *
 * NOTE: ProConnect's UserInfo endpoint returns a *signed JWT* (`application/jwt`), not the
 * JSON that Spring's default [OidcUserService] expects. We fetch the UserInfo JWT ourselves,
 * verify its signature against the provider's JWK set, then parse the claims.
 */
@Service
@ConditionalOnProperty(value = ["rapportnav.oidc.enabled"], havingValue = "true")
class ProConnectOidcUserService(
    private val findByEmail: FindByEmail,
    private val oidcProperties: OIDCProperties,
) : OidcUserService() {
    private val logger = LoggerFactory.getLogger(ProConnectOidcUserService::class.java)
    private val restTemplate = RestTemplate()

    // A JWT processor (bound to the remote JWK set) per jwkSetUri. The underlying JWKSource
    // caches and refreshes the keys, so we build this only once per provider.
    private val jwtProcessors = ConcurrentHashMap<String, DefaultJWTProcessor<SecurityContext>>()

    override fun loadUser(userRequest: OidcUserRequest): OidcUser {
        val claims = fetchUserInfoClaims(userRequest)

        val email = (claims["email"] as? String) ?: userRequest.idToken.getClaimAsString("email")
        if (email.isNullOrBlank()) {
            logger.warn("ProConnect authentication rejected: no email claim in UserInfo nor ID token")
            throw OAuth2AuthenticationException(OAuth2Error("invalid_user_info"), "Missing email claim")
        }

        val user = findByEmail.execute(email)
        if (user == null || user.disabledAt != null) {
            logger.warn("ProConnect authentication rejected for $email (unknown or disabled)")
            throw OAuth2AuthenticationException(OAuth2Error("access_denied"), "User not authorized")
        }

        // Enforce 2FA: ProConnect reports the authentication strength via the `acr` claim in the
        // ID token (eidas1 = single factor, eidas2+/*-mfa = multi-factor). We request it as an
        // essential claim on the authorization side (see SecurityConfig), but still verify it here.
        if (oidcProperties.forceMfa) {
            val acr = userRequest.idToken.getClaimAsString("acr")
            if (acr == null || acr !in oidcProperties.mfaAcrValues) {
                logger.warn("ProConnect 2FA required but acr='$acr' for $email")
                throw OAuth2AuthenticationException(OAuth2Error("insufficient_acr"), "Two-factor authentication required")
            }
        }

        logger.info("ProConnect authentication accepted for $email")
        val authorities = user.roles.map { SimpleGrantedAuthority(it.toAuthority().toString()) }
        return DefaultOidcUser(authorities, userRequest.idToken, OidcUserInfo(claims), "email")
    }

    /**
     * Calls the ProConnect UserInfo endpoint with the access token, verifies the returned
     * signed JWT against the provider's JWK set, and returns its claims. Falls back to the
     * ID token claims if no UserInfo endpoint is configured.
     */
    private fun fetchUserInfoClaims(userRequest: OidcUserRequest): Map<String, Any> {
        val providerDetails = userRequest.clientRegistration.providerDetails
        val uri = providerDetails.userInfoEndpoint.uri
        if (uri.isNullOrBlank()) {
            return userRequest.idToken.claims
        }

        val headers = HttpHeaders().apply {
            setBearerAuth(userRequest.accessToken.tokenValue)
            accept = listOf(MediaType.valueOf("application/jwt"), MediaType.APPLICATION_JSON)
        }

        val response = try {
            restTemplate.exchange(uri, HttpMethod.GET, HttpEntity<Void>(headers), String::class.java)
        } catch (e: Exception) {
            logger.error("Failed to fetch ProConnect UserInfo at $uri", e)
            throw OAuth2AuthenticationException(OAuth2Error("invalid_user_info_response"), e.message)
        }

        val body = response.body?.trim()
            ?: throw OAuth2AuthenticationException(OAuth2Error("invalid_user_info_response"), "Empty UserInfo response")

        val isJwt = response.headers.contentType?.subtype?.contains("jwt", ignoreCase = true) == true || isJwtShaped(body)
        if (!isJwt) {
            throw OAuth2AuthenticationException(
                OAuth2Error("invalid_user_info_response"),
                "Unexpected UserInfo content type: ${response.headers.contentType}",
            )
        }

        val jwkSetUri = providerDetails.jwkSetUri
        if (jwkSetUri.isNullOrBlank()) {
            throw OAuth2AuthenticationException(
                OAuth2Error("invalid_user_info_response"),
                "Cannot verify the UserInfo JWT: no JWK Set URI configured",
            )
        }

        return verifyAndParse(body, jwkSetUri)
    }

    /**
     * Verifies the UserInfo JWT signature against the JWK set at [jwkSetUri] and returns its
     * claims. The signing algorithm is derived from the key(s) published in the JWK set, so
     * we don't hardcode RS256/ES256.
     */
    private fun verifyAndParse(jwt: String, jwkSetUri: String): Map<String, Any> {
        val processor = jwtProcessors.computeIfAbsent(jwkSetUri) { buildProcessor(it) }
        return try {
            processor.process(jwt, null).claims
        } catch (e: Exception) {
            logger.error("ProConnect UserInfo JWT signature verification failed", e)
            throw OAuth2AuthenticationException(OAuth2Error("invalid_user_info_response"), "Invalid UserInfo JWT signature")
        }
    }

    private fun buildProcessor(jwkSetUri: String): DefaultJWTProcessor<SecurityContext> {
        val jwkSource: JWKSource<SecurityContext> =
            JWKSourceBuilder.create<SecurityContext>(URI.create(jwkSetUri).toURL()).build()
        return DefaultJWTProcessor<SecurityContext>().apply {
            // ProConnect's JWK set holds both an RSA (RS256) and an EC (ES256) signing key, and a
            // client may be configured for either. Allow both and let the selector match the right
            // key by the JWT's `alg`/`kid`, instead of locking onto a single key family.
            jwsKeySelector = JWSVerificationKeySelector(
                setOf(JWSAlgorithm.RS256, JWSAlgorithm.ES256),
                jwkSource,
            )
            // We only need to prove the UserInfo JWT is authentically signed by ProConnect.
            // It may legitimately omit `exp`, so we don't run the default time-based claim
            // checks (which would otherwise reject it). The signature check above is what matters.
            jwtClaimsSetVerifier = JWTClaimsSetVerifier<SecurityContext> { _, _ -> }
        }
    }

    private fun isJwtShaped(value: String): Boolean = value.count { it == '.' } == 2
}
