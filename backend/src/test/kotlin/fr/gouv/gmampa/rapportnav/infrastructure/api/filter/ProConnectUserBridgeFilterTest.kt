package fr.gouv.gmampa.rapportnav.infrastructure.api.filter

import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.FindByEmail
import fr.gouv.dgampa.rapportnav.infrastructure.api.filter.ProConnectUserBridgeFilter
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse

class ProConnectUserBridgeFilterTest {

    private val findByEmail: FindByEmail = mock()
    private val filter = ProConnectUserBridgeFilter(findByEmail)

    private fun oauthAuthentication(email: String): OAuth2AuthenticationToken {
        val idToken = OidcIdToken
            .withTokenValue("token")
            .claim("sub", "subject")
            .claim("email", email)
            .build()
        val oidcUser = DefaultOidcUser(listOf(SimpleGrantedAuthority("ROLE_PROCONNECT")), idToken, "email")
        return OAuth2AuthenticationToken(oidcUser, oidcUser.authorities, "proconnect")
    }

    private fun aUser(email: String) = User(
        id = 1,
        firstName = "jean",
        lastName = "dupont",
        email = email,
        password = "",
        roles = listOf(RoleTypeEnum.USER_PAM),
    )

    @AfterEach
    fun tearDown() = SecurityContextHolder.clearContext()

    @Test
    fun `bridges an OIDC session principal into the domain User on api requests`() {
        val email = "agent@mer.gouv.fr"
        whenever(findByEmail.execute(email)).thenReturn(aUser(email))

        // Simulate the context instance restored from the HTTP session.
        val sessionContext = SecurityContextHolder.createEmptyContext()
        val oauthAuthentication = oauthAuthentication(email)
        sessionContext.authentication = oauthAuthentication
        SecurityContextHolder.setContext(sessionContext)

        val request = MockHttpServletRequest("GET", "/api/v2/missions").apply { requestURI = "/api/v2/missions" }
        val response = MockHttpServletResponse()
        val chain: FilterChain = mock()

        filter.doFilter(request, response, chain)

        val authentication = SecurityContextHolder.getContext().authentication!!
        assertThat(authentication).isInstanceOf(UsernamePasswordAuthenticationToken::class.java)
        val principal = authentication.principal as User
        assertThat(principal.email).isEqualTo(email)
        assertThat(authentication.authorities.map { it.authority }).contains("ROLE_USER_PAM")
        verify(chain).doFilter(request, response)

        // Regression guard: the session-shared context must NOT be mutated in place, otherwise
        // the stored OIDC authentication is lost and ProConnect end-session on logout breaks.
        assertThat(sessionContext.authentication).isSameAs(oauthAuthentication)
    }

    @Test
    fun `rejects with 401 when the OIDC email is no longer authorized`() {
        val email = "ghost@mer.gouv.fr"
        whenever(findByEmail.execute(email)).thenReturn(null)
        SecurityContextHolder.getContext().authentication = oauthAuthentication(email)

        val request = MockHttpServletRequest("GET", "/api/v2/missions").apply { requestURI = "/api/v2/missions" }
        val response = MockHttpServletResponse()
        val chain: FilterChain = mock()

        filter.doFilter(request, response, chain)

        assertThat(response.status).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED)
        assertThat(SecurityContextHolder.getContext().authentication).isNull()
        verify(chain, never()).doFilter(any(), any())
    }

    @Test
    fun `leaves non-api requests untouched so OIDC logout keeps the original principal`() {
        val email = "agent@mer.gouv.fr"
        val oauth = oauthAuthentication(email)
        SecurityContextHolder.getContext().authentication = oauth

        val request = MockHttpServletRequest("POST", "/logout").apply { requestURI = "/logout" }
        val response = MockHttpServletResponse()
        val chain: FilterChain = mock()

        filter.doFilter(request, response, chain)

        assertThat(SecurityContextHolder.getContext().authentication).isSameAs(oauth)
        verify(chain).doFilter(request, response)
    }
}
