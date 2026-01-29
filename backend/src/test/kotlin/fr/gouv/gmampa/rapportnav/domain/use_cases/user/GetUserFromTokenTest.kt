package fr.gouv.gmampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetUserFromToken
import fr.gouv.gmampa.rapportnav.mocks.user.UserMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

class GetUserFromTokenTest {

    private val getUserFromToken = GetUserFromToken()

    @BeforeEach
    fun setUp() {
        SecurityContextHolder.clearContext()
    }

    @AfterEach
    fun tearDown() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun `should return user from security context`() {
        val user = UserMock.create(id = 1, email = "test@example.com")
        val authentication = UsernamePasswordAuthenticationToken(user, null, emptyList())
        SecurityContextHolder.getContext().authentication = authentication

        val result = getUserFromToken.execute()

        assertThat(result).isNotNull
        assertThat(result?.id).isEqualTo(1)
        assertThat(result?.email).isEqualTo("test@example.com")
    }

    @Test
    fun `should return null when no authentication`() {
        val result = getUserFromToken.execute()

        assertThat(result).isNull()
    }

    @Test
    fun `should return null when principal is not User type`() {
        val authentication = UsernamePasswordAuthenticationToken("stringPrincipal", null, emptyList())
        SecurityContextHolder.getContext().authentication = authentication

        val result = getUserFromToken.execute()

        assertThat(result).isNull()
    }
}
