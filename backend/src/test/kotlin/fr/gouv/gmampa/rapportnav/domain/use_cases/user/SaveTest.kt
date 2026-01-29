package fr.gouv.gmampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.Save
import fr.gouv.gmampa.rapportnav.mocks.user.UserMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [Save::class])
@ContextConfiguration(classes = [Save::class])
class SaveTest {

    @Autowired
    private lateinit var save: Save

    @MockitoBean
    private lateinit var userRepository: IUserRepository

    @Test
    fun `should save user successfully`() {
        val user = UserMock.create(id = 1)
        val savedUser = UserMock.create(id = 1)

        `when`(userRepository.save(any())).thenReturn(savedUser)

        val result = save.execute(user)

        assertThat(result).isNotNull
        assertThat(result?.id).isEqualTo(1)
    }

    @Test
    fun `should return null when repository returns null`() {
        val user = UserMock.create(id = 1)

        `when`(userRepository.save(any())).thenReturn(null)

        val result = save.execute(user)

        assertThat(result).isNull()
    }

    @Test
    fun `should propagate BackendUsageException from repository`() {
        val user = UserMock.create(id = 1)
        val originalException = BackendUsageException(
            code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
            message = "Repository error: invalid data"
        )

        `when`(userRepository.save(any())).thenAnswer { throw originalException }

        val exception = assertThrows<BackendUsageException> {
            save.execute(user)
        }
        assertThat(exception.code).isEqualTo(BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION)
    }

    @Test
    fun `should propagate BackendInternalException from repository`() {
        val user = UserMock.create(id = 1)
        val originalException = BackendInternalException(
            message = "Repository error",
            originalException = RuntimeException("Cause")
        )

        `when`(userRepository.save(any())).thenAnswer { throw originalException }

        val exception = assertThrows<BackendInternalException> {
            save.execute(user)
        }
        assertThat(exception.message).isEqualTo("Repository error")
    }
}
