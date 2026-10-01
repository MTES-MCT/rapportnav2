package fr.gouv.dgampa.rapportnav.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod

/**
 * Registers the ProConnect OIDC client. Only active when `rapportnav.oidc.enabled=true`,
 * so the legacy password/JWT flow is untouched when ProConnect is disabled.
 *
 * Pattern adapted from MonitorEnv's OIDCClientConfig.
 */
@Configuration
@ConditionalOnProperty(value = ["rapportnav.oidc.enabled"], havingValue = "true")
class OIDCClientConfig(
    private val oidcProperties: OIDCProperties,
) {
    private val logger: Logger = LoggerFactory.getLogger(OIDCClientConfig::class.java)

    @Bean
    fun clientRegistrationRepository(): ClientRegistrationRepository {
        logger.info("Creating ProConnect OIDC client registration")

        val builder =
            ClientRegistration
                .withRegistrationId("proconnect")
                .clientId(validate(oidcProperties.clientId, "Client ID"))
                .clientSecret(validate(oidcProperties.clientSecret, "Client Secret"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri(validate(oidcProperties.redirectUri, "Redirect URI"))
                .scope("openid", "email")
                .authorizationUri(validate(oidcProperties.authorizationUri, "Authorization URI"))
                .tokenUri(validate(oidcProperties.tokenUri, "Token URI"))
                .userInfoUri(validate(oidcProperties.userInfoUri, "User Info URI"))
                .userNameAttributeName("email")
                .jwkSetUri(validate(oidcProperties.jwkSetUri, "JWK Set URI"))
                .issuerUri(validate(oidcProperties.issuerUri, "Issuer URI"))
                .clientName("ProConnect")

        // Expose ProConnect's end_session endpoint in the provider metadata so
        // OidcClientInitiatedLogoutSuccessHandler can build the RP-initiated logout URL.
        // Without it, logout silently falls back to the default target ("/").
        if (oidcProperties.endSessionUri.isNotBlank()) {
            builder.providerConfigurationMetadata(
                mapOf("end_session_endpoint" to oidcProperties.endSessionUri),
            )
        }

        val clientRegistration = builder.build()

        logger.info("✅ ProConnect OIDC client registration created for client: ${oidcProperties.clientId}")
        return InMemoryClientRegistrationRepository(clientRegistration)
    }

    private fun validate(value: String, propertyName: String): String =
        value.takeIf { it.isNotBlank() }
            ?: throw IllegalArgumentException(
                "OIDC $propertyName is required but not configured. Please check your environment variables.",
            )
}
