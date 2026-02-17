package fr.gouv.gmampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.DisableUser
import fr.gouv.gmampa.rapportnav.mocks.user.UserMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant

@SpringBootTest(classes = [DisableUser::class])
@ContextConfiguration(classes = [DisableUser::class])
class DisableUserTest {

    @Autowired
    private lateinit var disableUser: DisableUser

    @MockitoBean
    private lateinit var userRepository: IUserRepository

    @Test
    fun `should disable user successfully`() {
        val disabledUser = UserMock.create(id = 1, disabledAt = Instant.now())

        `when`(userRepository.disable(1)).thenReturn(disabledUser)

        val result = disableUser.execute(1)

        assertThat(result).isNotNull
        assertThat(result?.id).isEqualTo(1)
        assertThat(result?.disabledAt).isNotNull
    }

    @Test
    fun `should return null when user not found`() {
        `when`(userRepository.disable(999)).thenReturn(null)

        val result = disableUser.execute(999)

        assertThat(result).isNull()
    }

    @Test
    fun `should propagate BackendInternalException from repository`() {
        val internalException = BackendInternalException(
            message = "Repository error",
            originalException = RuntimeException("Database error")
        )

        `when`(userRepository.disable(1)).thenAnswer { throw internalException }

        val exception = assertThrows<BackendInternalException> {
            disableUser.execute(1)
        }
        assertThat(exception.message).isEqualTo("Repository error")
    }
}