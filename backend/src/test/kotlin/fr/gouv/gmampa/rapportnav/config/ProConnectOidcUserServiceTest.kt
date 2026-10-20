package fr.gouv.gmampa.rapportnav.config

import fr.gouv.dgampa.rapportnav.config.OIDCProperties
import fr.gouv.dgampa.rapportnav.config.ProConnectOidcUserService
import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.FindByEmail
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import java.time.Instant

/**
 * Verifies the 2FA gate in ProConnectOidcUserService: when force-mfa is on, the `acr` claim in the
 * ID token must be one of the accepted MFA levels, otherwise login is rejected. The ClientRegistration
 * has no UserInfo endpoint, so loadUser reads claims straight from the ID token (no network call).
 */
class ProConnectOidcUserServiceTest {

    private val findByEmail: FindByEmail = mock()

    private fun registrationWithoutUserInfo(): ClientRegistration =
        ClientRegistration
            .withRegistrationId("proconnect")
            .clientId("test-client")
            .clientSecret("test-secret")
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri("http://localhost/login/oauth2/code/proconnect")
            .scope("openid", "email")
            .authorizationUri("https://proconnect.test/authorize")
            .tokenUri("https://proconnect.test/token")
            .build()

    private fun userRequest(acr: String?): OidcUserRequest {
        val now = Instant.parse("2026-10-02T00:00:00Z")
        val claims = buildMap<String, Any> {
            put("sub", "proconnect-sub")
            put("email", "agent@gouv.fr")
            if (acr != null) put("acr", acr)
        }
        val idToken = OidcIdToken("id-token-value", now, now.plusSeconds(3600), claims)
        val accessToken = OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "access-token", now, now.plusSeconds(3600))
        return OidcUserRequest(registrationWithoutUserInfo(), accessToken, idToken)
    }

    private fun validUser() = User(
        id = 1,
        firstName = "Agent",
        lastName = "Test",
        email = "agent@gouv.fr",
        password = "",
        roles = listOf(RoleTypeEnum.USER_PAM),
    )

    private fun service(forceMfa: Boolean) =
        ProConnectOidcUserService(findByEmail, OIDCProperties().apply { this.forceMfa = forceMfa })

    @Test
    fun `rejects single-factor acr (eidas1) when 2FA is forced`() {
        whenever(findByEmail.execute(any())).thenReturn(validUser())

        val ex = assertThrows(OAuth2AuthenticationException::class.java) {
            service(forceMfa = true).loadUser(userRequest(acr = "eidas1"))
        }
        assertEquals("insufficient_acr", ex.error.errorCode)
    }

    @Test
    fun `rejects missing acr when 2FA is forced`() {
        whenever(findByEmail.execute(any())).thenReturn(validUser())

        val ex = assertThrows(OAuth2AuthenticationException::class.java) {
            service(forceMfa = true).loadUser(userRequest(acr = null))
        }
        assertEquals("insufficient_acr", ex.error.errorCode)
    }

    @Test
    fun `accepts multi-factor acr (eidas2) when 2FA is forced`() {
        whenever(findByEmail.execute(any())).thenReturn(validUser())

        val user = service(forceMfa = true).loadUser(userRequest(acr = "eidas2"))

        assertEquals("agent@gouv.fr", user.email)
    }

    @Test
    fun `accepts OTP-Mail fallback acr (eidas1-mfa) when 2FA is forced`() {
        whenever(findByEmail.execute(any())).thenReturn(validUser())

        val user = service(forceMfa = true).loadUser(userRequest(acr = "eidas1-mfa"))

        assertEquals("agent@gouv.fr", user.email)
    }

    @Test
    fun `ignores acr entirely when 2FA is not forced`() {
        whenever(findByEmail.execute(any())).thenReturn(validUser())

        val user = service(forceMfa = false).loadUser(userRequest(acr = "eidas1"))

        assertEquals("agent@gouv.fr", user.email)
    }
}
