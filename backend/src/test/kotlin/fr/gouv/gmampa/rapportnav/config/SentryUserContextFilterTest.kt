package fr.gouv.gmampa.rapportnav.config

import fr.gouv.dgampa.rapportnav.config.SentryUserContextFilter
import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import io.sentry.Sentry
import io.sentry.protocol.User as SentryUser
import jakarta.servlet.FilterChain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.Mockito.mockStatic
import org.mockito.kotlin.*
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder

class SentryUserContextFilterTest {

    private lateinit var filter: SentryUserContextFilter
    private lateinit var request: MockHttpServletRequest
    private lateinit var response: MockHttpServletResponse
    private lateinit var filterChain: FilterChain
    private lateinit var sentryMock: MockedStatic<Sentry>

    @BeforeEach
    fun setUp() {
        filter = SentryUserContextFilter()
        request = MockHttpServletRequest()
        response = MockHttpServletResponse()
        filterChain = mock()
        SecurityContextHolder.clearContext()
        sentryMock = mockStatic(Sentry::class.java)
    }

    @AfterEach
    fun tearDown() {
        SecurityContextHolder.clearContext()
        sentryMock.close()
    }

    @Nested
    inner class PathFiltering {

        @Test
        fun `should skip filter for non-API paths`() {
            request.requestURI = "/index.html"
            setAuthenticatedUser()

            filter.doFilter(request, response, filterChain)

            // Filter skipped - no Sentry calls except possibly clearing
            verify(filterChain).doFilter(request, response)
            sentryMock.verify({ Sentry.setTag(any(), any()) }, Mockito.never())
        }

        @Test
        fun `should skip filter for root path`() {
            request.requestURI = "/"
            setAuthenticatedUser()

            filter.doFilter(request, response, filterChain)

            verify(filterChain).doFilter(request, response)
            sentryMock.verify({ Sentry.setTag(any(), any()) }, Mockito.never())
        }

        @Test
        fun `should skip filter for static resources`() {
            request.requestURI = "/static/js/main.js"
            setAuthenticatedUser()

            filter.doFilter(request, response, filterChain)

            verify(filterChain).doFilter(request, response)
            sentryMock.verify({ Sentry.setTag(any(), any()) }, Mockito.never())
        }

        @Test
        fun `should skip filter for swagger UI`() {
            request.requestURI = "/swagger-ui/index.html"
            setAuthenticatedUser()

            filter.doFilter(request, response, filterChain)

            verify(filterChain).doFilter(request, response)
            sentryMock.verify({ Sentry.setTag(any(), any()) }, Mockito.never())
        }

        @Test
        fun `should skip filter for login endpoint`() {
            request.requestURI = "/api/v1/auth/login"
            setAuthenticatedUser()

            filter.doFilter(request, response, filterChain)

            verify(filterChain).doFilter(request, response)
            sentryMock.verify({ Sentry.setTag(any(), any()) }, Mockito.never())
        }

        @Test
        fun `should run filter for API v2 endpoints`() {
            request.requestURI = "/api/v2/missions"
            setAuthenticatedUser()

            filter.doFilter(request, response, filterChain)

            verify(filterChain).doFilter(request, response)
            sentryMock.verify { Sentry.setTag(eq("roles"), any()) }
        }

        @Test
        fun `should run filter for API analytics endpoints`() {
            request.requestURI = "/api/analytics/data"
            setApiKeyAuthentication()

            filter.doFilter(request, response, filterChain)

            verify(filterChain).doFilter(request, response)
            sentryMock.verify { Sentry.setTag(eq("auth_type"), eq("api_key")) }
        }

        @Test
        fun `should run filter for API v1 non-login endpoints`() {
            request.requestURI = "/api/v1/users"
            setAuthenticatedUser()

            filter.doFilter(request, response, filterChain)

            verify(filterChain).doFilter(request, response)
            sentryMock.verify { Sentry.setTag(eq("roles"), any()) }
        }
    }

    @Nested
    inner class FilterChainBehavior {

        @Test
        fun `should always call filter chain`() {
            request.requestURI = "/api/v2/missions"

            filter.doFilter(request, response, filterChain)

            verify(filterChain).doFilter(request, response)
        }

        @Test
        fun `should call filter chain even when no authentication`() {
            request.requestURI = "/api/v2/missions"

            filter.doFilter(request, response, filterChain)

            verify(filterChain).doFilter(request, response)
        }

        @Test
        fun `should clear context even when exception in filter chain`() {
            request.requestURI = "/api/v2/missions"
            setAuthenticatedUser()
            whenever(filterChain.doFilter(any(), any())).thenThrow(RuntimeException("Error"))

            assertThrows(RuntimeException::class.java) {
                filter.doFilter(request, response, filterChain)
            }

            // Verify context was still cleared (via finally block)
            sentryMock.verify { Sentry.setUser(null) }
        }
    }

    @Nested
    inner class UserContext {

        @Test
        fun `should set user ID for authenticated user`() {
            request.requestURI = "/api/v2/missions"
            setAuthenticatedUser(id = 42)

            filter.doFilter(request, response, filterChain)

            val userCaptor = ArgumentCaptor.forClass(SentryUser::class.java)
            sentryMock.verify({ Sentry.setUser(userCaptor.capture()) }, Mockito.atLeastOnce())

            val capturedUsers = userCaptor.allValues.filterNotNull()
            assertTrue(capturedUsers.any { it.id == "42" })
        }

        @Test
        fun `should set service ID tag when present`() {
            request.requestURI = "/api/v2/missions"
            setAuthenticatedUser(id = 1, serviceId = 123)

            filter.doFilter(request, response, filterChain)

            sentryMock.verify { Sentry.setTag("service_id", "123") }
        }

        @Test
        fun `should not set service ID tag when null`() {
            request.requestURI = "/api/v2/missions"
            setAuthenticatedUser(id = 1, serviceId = null)

            filter.doFilter(request, response, filterChain)

            sentryMock.verify({ Sentry.setTag(eq("service_id"), any()) }, Mockito.never())
        }

        @Test
        fun `should set roles tag`() {
            request.requestURI = "/api/v2/missions"
            setAuthenticatedUser(roles = listOf(RoleTypeEnum.USER_PAM, RoleTypeEnum.ADMIN))

            filter.doFilter(request, response, filterChain)

            sentryMock.verify { Sentry.setTag("roles", "USER_PAM,ADMIN") }
        }

        @Test
        fun `should clear context after request completes`() {
            request.requestURI = "/api/v2/missions"
            setAuthenticatedUser()

            filter.doFilter(request, response, filterChain)

            sentryMock.verify { Sentry.setUser(null) }
            sentryMock.verify { Sentry.removeTag("service_id") }
            sentryMock.verify { Sentry.removeTag("roles") }
        }
    }

    @Nested
    inner class GdprCompliance {

        @Test
        fun `should NOT send email to Sentry`() {
            request.requestURI = "/api/v2/missions"
            setAuthenticatedUser(email = "sensitive@email.com")

            filter.doFilter(request, response, filterChain)

            val userCaptor = ArgumentCaptor.forClass(SentryUser::class.java)
            sentryMock.verify({ Sentry.setUser(userCaptor.capture()) }, Mockito.atLeastOnce())

            val capturedUsers = userCaptor.allValues.filterNotNull()
            assertTrue(capturedUsers.isNotEmpty(), "Should have captured at least one user")
            capturedUsers.forEach { sentryUser ->
                assertNull(sentryUser.email, "Email should not be sent to Sentry (GDPR)")
            }
        }

        @Test
        fun `should NOT send username to Sentry`() {
            request.requestURI = "/api/v2/missions"
            setAuthenticatedUser(firstName = "John", lastName = "Doe")

            filter.doFilter(request, response, filterChain)

            val userCaptor = ArgumentCaptor.forClass(SentryUser::class.java)
            sentryMock.verify({ Sentry.setUser(userCaptor.capture()) }, Mockito.atLeastOnce())

            val capturedUsers = userCaptor.allValues.filterNotNull()
            assertTrue(capturedUsers.isNotEmpty(), "Should have captured at least one user")
            capturedUsers.forEach { sentryUser ->
                assertNull(sentryUser.username, "Username should not be sent to Sentry (GDPR)")
            }
        }

        @Test
        fun `should only send pseudonymized user ID`() {
            request.requestURI = "/api/v2/missions"
            setAuthenticatedUser(
                id = 99,
                email = "sensitive@email.com",
                firstName = "John",
                lastName = "Doe"
            )

            filter.doFilter(request, response, filterChain)

            val userCaptor = ArgumentCaptor.forClass(SentryUser::class.java)
            sentryMock.verify({ Sentry.setUser(userCaptor.capture()) }, Mockito.atLeastOnce())

            val capturedUsers = userCaptor.allValues.filterNotNull()
            val userWithId = capturedUsers.find { it.id == "99" }
            assertNotNull(userWithId, "User ID should be set")
            assertNull(userWithId?.email, "Email must not be set")
            assertNull(userWithId?.username, "Username must not be set")
        }
    }

    @Nested
    inner class ApiKeyContext {

        @Test
        fun `should set API key context for string principal`() {
            request.requestURI = "/api/analytics/data"
            setApiKeyAuthentication(publicId = "abc123def456")

            filter.doFilter(request, response, filterChain)

            val userCaptor = ArgumentCaptor.forClass(SentryUser::class.java)
            sentryMock.verify({ Sentry.setUser(userCaptor.capture()) }, Mockito.atLeastOnce())

            val capturedUsers = userCaptor.allValues.filterNotNull()
            assertTrue(capturedUsers.any { it.id == "abc123def456" })
        }

        @Test
        fun `should set auth_type tag for API key`() {
            request.requestURI = "/api/analytics/data"
            setApiKeyAuthentication()

            filter.doFilter(request, response, filterChain)

            sentryMock.verify { Sentry.setTag("auth_type", "api_key") }
        }

        @Test
        fun `should set api_key_public_id tag`() {
            request.requestURI = "/api/analytics/data"
            setApiKeyAuthentication(publicId = "pub123")

            filter.doFilter(request, response, filterChain)

            sentryMock.verify { Sentry.setTag("api_key_public_id", "pub123") }
        }

        @Test
        fun `should clear API key context after request`() {
            request.requestURI = "/api/analytics/data"
            setApiKeyAuthentication()

            filter.doFilter(request, response, filterChain)

            sentryMock.verify { Sentry.setUser(null) }
            sentryMock.verify { Sentry.removeTag("auth_type") }
            sentryMock.verify { Sentry.removeTag("api_key_public_id") }
        }

        @Test
        fun `should NOT expose owner name for API key (GDPR)`() {
            request.requestURI = "/api/analytics/data"
            setApiKeyAuthentication(owner = "John Doe Company", publicId = "pub123")

            filter.doFilter(request, response, filterChain)

            val userCaptor = ArgumentCaptor.forClass(SentryUser::class.java)
            sentryMock.verify({ Sentry.setUser(userCaptor.capture()) }, Mockito.atLeastOnce())

            val capturedUsers = userCaptor.allValues.filterNotNull()
            assertTrue(capturedUsers.isNotEmpty(), "Should have captured at least one user")
            capturedUsers.forEach { sentryUser ->
                // Username should not be set, only ID with publicId
                assertNull(sentryUser.username, "Owner name should not be sent as username")
                assertEquals("pub123", sentryUser.id, "Only public ID should be used as user ID")
            }
        }
    }

    @Nested
    inner class NoAuthentication {

        @Test
        fun `should not set Sentry user context when not authenticated`() {
            request.requestURI = "/api/v2/missions"
            // No authentication set

            filter.doFilter(request, response, filterChain)

            // Should only be called with null (clearing), not with actual user
            sentryMock.verify { Sentry.setUser(null) }
            sentryMock.verify({ Sentry.setTag(eq("roles"), any()) }, Mockito.never())
        }

        @Test
        fun `should still clear context when not authenticated`() {
            request.requestURI = "/api/v2/missions"

            filter.doFilter(request, response, filterChain)

            sentryMock.verify { Sentry.removeTag("service_id") }
            sentryMock.verify { Sentry.removeTag("roles") }
            sentryMock.verify { Sentry.removeTag("auth_type") }
            sentryMock.verify { Sentry.removeTag("api_key_public_id") }
        }
    }

    @Nested
    inner class ContextIsolation {

        @Test
        fun `should clear all tags to prevent context leakage between requests`() {
            request.requestURI = "/api/v2/missions"
            setAuthenticatedUser(serviceId = 1, roles = listOf(RoleTypeEnum.ADMIN))

            filter.doFilter(request, response, filterChain)

            // Verify all possible tags are cleared
            sentryMock.verify { Sentry.removeTag("service_id") }
            sentryMock.verify { Sentry.removeTag("roles") }
            sentryMock.verify { Sentry.removeTag("auth_type") }
            sentryMock.verify { Sentry.removeTag("api_key_public_id") }
        }
    }

    // Helper methods

    private fun setAuthenticatedUser(
        id: Int = 1,
        serviceId: Int? = null,
        firstName: String = "Test",
        lastName: String = "User",
        email: String = "test@test.com",
        roles: List<RoleTypeEnum> = listOf(RoleTypeEnum.USER_PAM)
    ) {
        val user = User(
            id = id,
            serviceId = serviceId,
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = "password",
            roles = roles
        )
        val authorities = roles.map { SimpleGrantedAuthority("ROLE_$it") }
        val authentication = UsernamePasswordAuthenticationToken(user, "", authorities)
        SecurityContextHolder.getContext().authentication = authentication
    }

    private fun setApiKeyAuthentication(
        owner: String = "api-user",
        publicId: String = "test-public-id"
    ) {
        val authorities = listOf(SimpleGrantedAuthority("ROLE_API_USER"))
        val authentication = UsernamePasswordAuthenticationToken(owner, null, authorities)
        authentication.details = mapOf(
            "publicId" to publicId,
            "apiKeyId" to "internal-uuid"
        )
        SecurityContextHolder.getContext().authentication = authentication
    }
}