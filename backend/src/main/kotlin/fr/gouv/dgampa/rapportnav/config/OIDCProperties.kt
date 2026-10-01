package fr.gouv.dgampa.rapportnav.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 * ProConnect (OIDC) configuration properties, bound from `rapportnav.oidc.*`.
 *
 * When `enabled` is false (the default), no ProConnect beans are created and the
 * application keeps behaving exactly as the legacy email/password + JWT flow.
 */
@Configuration
@ConfigurationProperties(prefix = "rapportnav.oidc")
class OIDCProperties {
    var enabled: Boolean = false
    var clientId: String = ""
    var clientSecret: String = ""
    var redirectUri: String = ""
    var issuerUri: String = ""
    var authorizationUri: String = ""
    var tokenUri: String = ""
    var userInfoUri: String = ""
    var jwkSetUri: String = ""
    // ProConnect end_session endpoint — required for RP-initiated logout (end-session).
    var endSessionUri: String = ""

    // Frontend URLs the backend redirects the browser to after the OIDC dance.
    var successUrl: String = "/"
    var errorUrl: String = "/login?error"

    // Where ProConnect sends the browser after end-session (must be an absolute URL that
    // reaches the frontend, and registered as a post_logout_redirect_uri in the portal).
    // Supports Spring's "{baseUrl}" template for same-origin prod deployments.
    var postLogoutRedirectUri: String = "{baseUrl}/login"
}
