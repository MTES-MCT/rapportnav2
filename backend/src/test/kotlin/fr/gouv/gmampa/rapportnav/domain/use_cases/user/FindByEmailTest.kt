package fr.gouv.gmampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.FindByEmail
import fr.gouv.gmampa.rapportnav.mocks.user.UserMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [FindByEmail::class])
@ContextConfiguration(classes = [FindByEmail::class])
class FindByEmailTest {

    @Autowired
    private lateinit var findByEmail: FindByEmail

    @MockitoBean
    private lateinit var userRepository: IUserRepository

    @Test
    fun `should return user when found by email`() {
        val email = "test@example.com"
        val user = UserMock.create(id = 1, email = email)

        `when`(userRepository.findByEmail(email)).thenReturn(user)

        val result = findByEmail.execute(email)

        assertThat(result).isNotNull
        assertThat(result?.email).isEqualTo(email)
    }

    @Test
    fun `should return null when user not found`() {
        val email = "notfound@example.com"

        `when`(userRepository.findByEmail(email)).thenReturn(null)

        val result = findByEmail.execute(email)

        assertThat(result).isNull()
    }

    @Test
    fun `should throw BackendInternalException when repository fails`() {
        val email = "test@example.com"

        `when`(userRepository.findByEmail(email)).thenAnswer { throw RuntimeException("Database error") }

        val exception = assertThrows<BackendInternalException> {
            findByEmail.execute(email)
        }
        assertThat(exception.message).contains("FindByEmail failed for email=$email")
    }
}
