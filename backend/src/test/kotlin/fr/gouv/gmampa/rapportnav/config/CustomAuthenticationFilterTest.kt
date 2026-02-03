package fr.gouv.gmampa.rapportnav.config

import fr.gouv.dgampa.rapportnav.config.CustomAuthenticationFilter
import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import jakarta.servlet.FilterChain
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder

class CustomAuthenticationFilterTest {
    private lateinit var tokenService: TokenService
    private lateinit var filter: CustomAuthenticationFilter
    private lateinit var request: MockHttpServletRequest
    private lateinit var response: MockHttpServletResponse
    private lateinit var filterChain: FilterChain

    @BeforeEach
    fun setUp() {
        tokenService = mock()
        filter = CustomAuthenticationFilter(tokenService)
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

        filter.doFilter(request, response, filterChain)

        val auth = SecurityContextHolder.getContext().authentication
        assertNotNull(auth)
        verify(tokenService).parseToken("my-special-token-123")
    }
}