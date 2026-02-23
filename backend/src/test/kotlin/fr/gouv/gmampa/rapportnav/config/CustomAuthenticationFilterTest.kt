package fr.gouv.gmampa.rapportnav.config

import fr.gouv.dgampa.rapportnav.config.CustomAuthenticationFilter
import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendForbiddenErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendForbiddenException
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.ProcessImpersonationRequest
import jakarta.servlet.FilterChain
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder

class CustomAuthenticationFilterTest {
    private lateinit var tokenService: TokenService
    private lateinit var processImpersonationRequest: ProcessImpersonationRequest
    private lateinit var filter: CustomAuthenticationFilter
    private lateinit var request: MockHttpServletRequest
    private lateinit var response: MockHttpServletResponse
    private lateinit var filterChain: FilterChain

    @BeforeEach
    fun setUp() {
        tokenService = mock()
        processImpersonationRequest = mock()
        filter = CustomAuthenticationFilter(tokenService, processImpersonationRequest)
        request = MockHttpServletRequest()
        response = MockHttpServletResponse()
        filterChain = mock()
        SecurityContextHolder.clearContext()
    }

    @Test
    fun `should authenticate valid token and set security context`() {
        val user = User(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@test.com",
            password = "password",
            roles = listOf(RoleTypeEnum.USER_PAM)
        )

        request.addHeader("Authorization", "Bearer valid-token")
        whenever(tokenService.parseToken("valid-token")).thenReturn(user)
        whenever(processImpersonationRequest.execute(eq(user), anyOrNull(), anyOrNull())).thenReturn(user)

        filter.doFilter(request, response, filterChain)

        val auth = SecurityContextHolder.getContext().authentication
        assertNotNull(auth)
        assertEquals(user, auth?.principal)
        assertTrue(auth?.authorities?.any { it.authority == "ROLE_USER_PAM" } == true)
        verify(filterChain).doFilter(request, response)
    }

    @Test
    fun `should handle user with multiple roles`() {
        val user = User(
            id = 1,
            firstName = "Admin",
            lastName = "User",
            email = "admin@test.com",
            password = "password",
            roles = listOf(RoleTypeEnum.ADMIN, RoleTypeEnum.USER_PAM)
        )

        request.addHeader("Authorization", "Bearer valid-token")
        whenever(tokenService.parseToken("valid-token")).thenReturn(user)
        whenever(processImpersonationRequest.execute(eq(user), anyOrNull(), anyOrNull())).thenReturn(user)

        filter.doFilter(request, response, filterChain)

        val auth = SecurityContextHolder.getContext().authentication
        assertNotNull(auth)
        assertEquals(2, auth?.authorities?.size)
        assertTrue(auth?.authorities?.any { it.authority == "ROLE_ADMIN" } == true)
        assertTrue(auth?.authorities?.any { it.authority == "ROLE_USER_PAM" } == true)
        verify(filterChain).doFilter(request, response)
    }

    @Test
    fun `should proceed without authentication when Authorization header is missing`() {
        filter.doFilter(request, response, filterChain)

        val auth = SecurityContextHolder.getContext().authentication
        assertNull(auth)
        verify(filterChain).doFilter(request, response)
    }

    @Test
    fun `should proceed without authentication when Authorization header does not start with Bearer`() {
        request.addHeader("Authorization", "Basic some-credentials")

        filter.doFilter(request, response, filterChain)

        val auth = SecurityContextHolder.getContext().authentication
        assertNull(auth)
        verify(filterChain).doFilter(request, response)
    }

    @Test
    fun `should clear context and proceed when token parsing fails`() {
        request.addHeader("Authorization", "Bearer invalid-token")
        whenever(tokenService.parseToken("invalid-token")).thenThrow(RuntimeException("Invalid token"))

        filter.doFilter(request, response, filterChain)

        val auth = SecurityContextHolder.getContext().authentication
        assertNull(auth)
        verify(filterChain).doFilter(request, response)
    }

    @Test
    fun `should proceed without authentication when parseToken returns null`() {
        request.addHeader("Authorization", "Bearer unknown-token")
        whenever(tokenService.parseToken("unknown-token")).thenReturn(null)

        filter.doFilter(request, response, filterChain)

        val auth = SecurityContextHolder.getContext().authentication
        assertNull(auth)
        verify(filterChain).doFilter(request, response)
    }

    @Test
    fun `should extract token correctly from Bearer header`() {
        val user = User(
            id = 1,
            firstName = "Test",
            lastName = "User",
            email = "test@test.com",
            password = "password",
            roles = listOf(RoleTypeEnum.USER_ULAM)
        )

        request.addHeader("Authorization", "Bearer my-special-token-123")
        whenever(tokenService.parseToken("my-special-token-123")).thenReturn(user)
        whenever(processImpersonationRequest.execute(eq(user), anyOrNull(), anyOrNull())).thenReturn(user)

        filter.doFilter(request, response, filterChain)

        val auth = SecurityContextHolder.getContext().authentication
        assertNotNull(auth)
        verify(tokenService).parseToken("my-special-token-123")
    }

    @Test
    fun `should delegate impersonation to ProcessImpersonationRequest use case`() {
        val adminUser = User(
            id = 1,
            serviceId = 100,
            firstName = "Admin",
            lastName = "User",
            email = "admin@test.com",
            password = "password",
            roles = listOf(RoleTypeEnum.ADMIN)
        )
        // When impersonating, ROLE_ADMIN should be removed
        val impersonatedUser = adminUser.copy(serviceId = 200, roles = listOf(RoleTypeEnum.USER_PAM))

        request.addHeader("Authorization", "Bearer valid-token")
        request.addHeader("X-Impersonate-Service-Id", "200")
        whenever(tokenService.parseToken("valid-token")).thenReturn(adminUser)
        whenever(processImpersonationRequest.execute(eq(adminUser), eq(200), anyOrNull()))
            .thenReturn(impersonatedUser)

        filter.doFilter(request, response, filterChain)

        val auth = SecurityContextHolder.getContext().authentication
        assertNotNull(auth)
        val principal = auth?.principal as User
        assertEquals(200, principal.serviceId)
        // Should not have ROLE_ADMIN anymore
        assertFalse(auth.authorities.any { it.authority == "ROLE_ADMIN" })
        verify(processImpersonationRequest).execute(eq(adminUser), eq(200), anyOrNull())
        verify(filterChain).doFilter(request, response)
    }

    @Test
    fun `should return 403 when non-admin attempts impersonation`() {
        val regularUser = User(
            id = 1,
            serviceId = 100,
            firstName = "User",
            lastName = "Test",
            email = "user@test.com",
            password = "password",
            roles = listOf(RoleTypeEnum.USER_PAM)
        )

        request.addHeader("Authorization", "Bearer valid-token")
        request.addHeader("X-Impersonate-Service-Id", "200")
        whenever(tokenService.parseToken("valid-token")).thenReturn(regularUser)
        whenever(processImpersonationRequest.execute(eq(regularUser), eq(200), anyOrNull()))
            .thenThrow(BackendForbiddenException(BackendForbiddenErrorCode.UNAUTHORIZED_IMPERSONATION, "Impersonation requires ADMIN role"))

        filter.doFilter(request, response, filterChain)

        assertEquals(HttpStatus.FORBIDDEN.value(), response.status)
        assertTrue(response.contentAsString.contains("Forbidden"))
        // Filter chain should NOT be called when returning 403
        verify(filterChain, never()).doFilter(any(), any())
    }

    @Test
    fun `should return 403 when impersonation validation fails`() {
        val adminUser = User(
            id = 1,
            serviceId = 100,
            firstName = "Admin",
            lastName = "User",
            email = "admin@test.com",
            password = "password",
            roles = listOf(RoleTypeEnum.ADMIN)
        )

        request.addHeader("Authorization", "Bearer valid-token")
        request.addHeader("X-Impersonate-Service-Id", "999")
        whenever(tokenService.parseToken("valid-token")).thenReturn(adminUser)
        whenever(processImpersonationRequest.execute(eq(adminUser), eq(999), anyOrNull()))
            .thenThrow(BackendForbiddenException(BackendForbiddenErrorCode.UNAUTHORIZED_IMPERSONATION, "Invalid impersonation target: Service not found"))

        filter.doFilter(request, response, filterChain)

        assertEquals(HttpStatus.FORBIDDEN.value(), response.status)
        assertTrue(response.contentAsString.contains("Forbidden"))
        verify(filterChain, never()).doFilter(any(), any())
    }

    @Test
    fun `should pass null targetServiceId when header is missing`() {
        val user = User(
            id = 1,
            serviceId = 100,
            firstName = "User",
            lastName = "Test",
            email = "user@test.com",
            password = "password",
            roles = listOf(RoleTypeEnum.USER_PAM)
        )

        request.addHeader("Authorization", "Bearer valid-token")
        whenever(tokenService.parseToken("valid-token")).thenReturn(user)
        whenever(processImpersonationRequest.execute(eq(user), eq(null), anyOrNull())).thenReturn(user)

        filter.doFilter(request, response, filterChain)

        verify(processImpersonationRequest).execute(eq(user), eq(null), anyOrNull())
        verify(filterChain).doFilter(request, response)
    }

    @Test
    fun `should pass null targetServiceId when header is not a valid integer`() {
        val user = User(
            id = 1,
            serviceId = 100,
            firstName = "Admin",
            lastName = "User",
            email = "admin@test.com",
            password = "password",
            roles = listOf(RoleTypeEnum.ADMIN)
        )

        request.addHeader("Authorization", "Bearer valid-token")
        request.addHeader("X-Impersonate-Service-Id", "invalid")
        whenever(tokenService.parseToken("valid-token")).thenReturn(user)
        whenever(processImpersonationRequest.execute(eq(user), eq(null), anyOrNull())).thenReturn(user)

        filter.doFilter(request, response, filterChain)

        verify(processImpersonationRequest).execute(eq(user), eq(null), anyOrNull())
        verify(filterChain).doFilter(request, response)
    }
}
