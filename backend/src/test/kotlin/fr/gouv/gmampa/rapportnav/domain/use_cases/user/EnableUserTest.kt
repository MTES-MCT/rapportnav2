package fr.gouv.gmampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.EnableUser
import fr.gouv.gmampa.rapportnav.mocks.user.UserMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [EnableUser::class])
@ContextConfiguration(classes = [EnableUser::class])
class EnableUserTest {

    @Autowired
    private lateinit var enableUser: EnableUser

    @MockitoBean
    private lateinit var userRepository: IUserRepository

    @Test
    fun `should enable user successfully`() {
        val enabledUser = UserMock.create(id = 1, disabledAt = null)

        `when`(userRepository.enable(1)).thenReturn(enabledUser)

        val result = enableUser.execute(1)

        assertThat(result).isNotNull
        assertThat(result?.id).isEqualTo(1)
        assertThat(result?.disabledAt).isNull()
    }

    @Test
    fun `should return null when user not found`() {
        `when`(userRepository.enable(999)).thenReturn(null)

        val result = enableUser.execute(999)

        assertThat(result).isNull()
    }

    @Test
    fun `should propagate BackendInternalException from repository`() {
        val internalException = BackendInternalException(
            message = "Repository error",
            originalException = RuntimeException("Database error")
        )

        `when`(userRepository.enable(1)).thenAnswer { throw internalException }

        val exception = assertThrows<BackendInternalException> {
            enableUser.execute(1)
        }
        assertThat(exception.message).isEqualTo("Repository error")
    }
}