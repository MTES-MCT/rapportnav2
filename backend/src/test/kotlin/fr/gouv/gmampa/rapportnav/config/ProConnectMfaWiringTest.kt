package fr.gouv.gmampa.rapportnav.config

import fr.gouv.dgampa.rapportnav.config.OIDCProperties
import fr.gouv.dgampa.rapportnav.config.ProConnectOidcUserService
import fr.gouv.dgampa.rapportnav.config.SecurityConfig
import fr.gouv.dgampa.rapportnav.infrastructure.api.filter.ApiKeyAuthenticationFilter
import fr.gouv.dgampa.rapportnav.infrastructure.api.filter.CustomAuthenticationFilter
import fr.gouv.dgampa.rapportnav.infrastructure.api.filter.ProConnectUserBridgeFilter
import fr.gouv.dgampa.rapportnav.infrastructure.api.filter.SentryUserContextFilter
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.web.context.WebApplicationContext
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

/**
 * Shared test infra: a real "proconnect" client registration plus mocked filters/services, so the
 * full SecurityConfig filter chain can be built. 2FA enforcement is driven through the real
 * rapportnav.oidc.* property binding (as in production), not an overriding bean.
 */
@TestConfiguration
@EnableConfigurationProperties(OIDCProperties::class)
class ProConnectMfaTestSupport {
    @Bean
    fun customAuthenticationFilter(): CustomAuthenticationFilter = mock()

    @Bean
    fun apiKeyAuthenticationFilter(): ApiKeyAuthenticationFilter = mock()

    @Bean
    fun sentryUserContextFilter(): SentryUserContextFilter = mock()

    @Bean
    fun proConnectUserBridgeFilter(): ProConnectUserBridgeFilter = mock()

    @Bean
    fun proConnectOidcUserService(): ProConnectOidcUserService = mock()

    @Bean
    fun clientRegistrationRepository(): ClientRegistrationRepository =
        InMemoryClientRegistrationRepository(
            ClientRegistration
                .withRegistrationId("proconnect")
                .clientId("test-client")
                .clientSecret("test-secret")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost/login/oauth2/code/proconnect")
                .scope("openid", "email")
                .authorizationUri("https://proconnect.test/authorize")
                .tokenUri("https://proconnect.test/token")
                .userInfoUri("https://proconnect.test/userinfo")
                .userNameAttributeName("email")
                .jwkSetUri("https://proconnect.test/jwks")
                .clientName("ProConnect")
                .build(),
        )
}

/** 2FA enforced: the authorization redirect must carry the essential-acr `claims` parameter. */
@WebMvcTest
@Import(ProConnectMfaTestSupport::class, SecurityConfig::class)
@ContextConfiguration(classes = [ProConnectMfaTestSupport::class, SecurityConfig::class])
@TestPropertySource(properties = ["rapportnav.oidc.enabled=true", "rapportnav.oidc.force-mfa=true"])
class ProConnectMfaForcedTest {

    @Autowired
    lateinit var context: WebApplicationContext
    lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply<DefaultMockMvcBuilder>(springSecurity()).build()
    }

    @Test
    fun `authorization redirect carries essential-acr claims when 2FA is forced`() {
        val location = mockMvc.perform(get("/oauth2/authorization/proconnect"))
            .andReturn().response.getHeader("Location")

        assertNotNull(location, "Should redirect to the ProConnect authorize endpoint")
        val decoded = URLDecoder.decode(location!!, StandardCharsets.UTF_8)
        assertTrue(decoded.startsWith("https://proconnect.test/authorize"), "Should target ProConnect authorize, got: $decoded")
        assertTrue(
            decoded.contains("""claims={"id_token":{"acr":{"essential":true,"values":["eidas0-mfa","eidas1-mfa","eidas2","eidas3"]}}}"""),
            "claims parameter should mark acr as essential with the MFA set, got: $decoded",
        )
    }
}

/** 2FA disabled (default): no `claims` parameter is added — behaviour is unchanged. */
@WebMvcTest
@Import(ProConnectMfaTestSupport::class, SecurityConfig::class)
@ContextConfiguration(classes = [ProConnectMfaTestSupport::class, SecurityConfig::class])
@TestPropertySource(properties = ["rapportnav.oidc.enabled=true", "rapportnav.oidc.force-mfa=false"])
class ProConnectMfaNotForcedTest {

    @Autowired
    lateinit var context: WebApplicationContext
    lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply<DefaultMockMvcBuilder>(springSecurity()).build()
    }

    @Test
    fun `authorization redirect omits claims parameter when 2FA is not forced`() {
        val location = mockMvc.perform(get("/oauth2/authorization/proconnect"))
            .andReturn().response.getHeader("Location")

        assertNotNull(location, "Should redirect to the ProConnect authorize endpoint")
        val decoded = URLDecoder.decode(location!!, StandardCharsets.UTF_8)
        assertTrue(decoded.startsWith("https://proconnect.test/authorize"), "Should target ProConnect authorize, got: $decoded")
        assertFalse(decoded.contains("\"acr\""), "claims parameter should be absent when 2FA is disabled, got: $decoded")
    }
}
