package fr.gouv.gmampa.rapportnav.config

import fr.gouv.dgampa.rapportnav.config.AuditorAwareImpl
import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

class AuditorAwareImplTest {

    private lateinit var auditorAware: AuditorAwareImpl

    @BeforeEach
    fun setUp() {
        auditorAware = AuditorAwareImpl()
        SecurityContextHolder.clearContext()
    }

    @AfterEach
    fun tearDown() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun `should return user id when principal is User with id`() {
        val user = User(
            id = 42,
            firstName = "John",
            lastName = "Doe",
            email = "john@example.com",
            password = "password",
            roles = listOf(RoleTypeEnum.USER_PAM)
        )
        val auth = UsernamePasswordAuthenticationToken(user, null, emptyList())
        SecurityContextHolder.getContext().authentication = auth

        val result = auditorAware.currentAuditor

        assertTrue(result.isPresent)
        assertEquals(42, result.get())
    }

    @Test
    fun `should return empty optional when principal is User with null id`() {
        val user = User(
            id = null,
            firstName = "John",
            lastName = "Doe",
            email = "john@example.com",
            password = "password",
            roles = listOf(RoleTypeEnum.USER_PAM)
        )
        val auth = UsernamePasswordAuthenticationToken(user, null, emptyList())
        SecurityContextHolder.getContext().authentication = auth

        val result = auditorAware.currentAuditor

        assertTrue(result.isEmpty)
    }

    @Test
    fun `should return default value when principal is String`() {
        val auth = UsernamePasswordAuthenticationToken("anonymousUser", null, emptyList())
        SecurityContextHolder.getContext().authentication = auth

        val result = auditorAware.currentAuditor

        assertTrue(result.isPresent)
        assertEquals(-1, result.get())
    }

    @Test
    fun `should return default value when authentication is null`() {
        SecurityContextHolder.getContext().authentication = null

        val result = auditorAware.currentAuditor

        assertTrue(result.isPresent)
        assertEquals(-1, result.get())
    }

    @Test
    fun `should return default value when principal is other type`() {
        val auth = UsernamePasswordAuthenticationToken(12345, null, emptyList())
        SecurityContextHolder.getContext().authentication = auth

        val result = auditorAware.currentAuditor

        assertTrue(result.isPresent)
        assertEquals(-1, result.get())
    }
}
