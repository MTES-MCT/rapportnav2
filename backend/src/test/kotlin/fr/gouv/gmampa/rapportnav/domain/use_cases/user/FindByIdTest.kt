package fr.gouv.gmampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.FindById
import fr.gouv.gmampa.rapportnav.mocks.user.UserMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [FindById::class])
@ContextConfiguration(classes = [FindById::class])
class FindByIdTest {

    @Autowired
    private lateinit var findById: FindById

    @MockitoBean
    private lateinit var userRepository: IUserRepository

    @Test
    fun `should return user when found`() {
        val userId = 1
        val user = UserMock.create(id = userId, serviceId = 10)

        `when`(userRepository.findById(userId)).thenReturn(user)

        val result = findById.execute(userId)

        assertThat(result).isNotNull
        assertThat(result?.id).isEqualTo(userId)
        assertThat(result?.serviceId).isEqualTo(10)
    }

    @Test
    fun `should return null when user not found`() {
        val userId = 999

        `when`(userRepository.findById(userId)).thenReturn(null)

        val result = findById.execute(userId)

        assertThat(result).isNull()
    }

    @Test
    fun `should propagate BackendInternalException from repository`() {
        val userId = 1
        val originalException = BackendInternalException(
            message = "Repository error",
            originalException = RuntimeException("Cause")
        )

        `when`(userRepository.findById(userId)).thenAnswer { throw originalException }

        val exception = assertThrows<BackendInternalException> {
            findById.execute(userId)
        }
        assertThat(exception.message).isEqualTo("Repository error")
    }
}
