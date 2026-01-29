package fr.gouv.gmampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.HashService
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.UpdateUserPassword
import fr.gouv.dgampa.rapportnav.infrastructure.api.auth.adapters.inputs.UpdateUserPasswordInput
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

@SpringBootTest(classes = [UpdateUserPassword::class])
@ContextConfiguration(classes = [UpdateUserPassword::class])
class UpdateUserPasswordTest {

    @Autowired
    private lateinit var updateUserPassword: UpdateUserPassword

    @MockitoBean
    private lateinit var userRepository: IUserRepository

    @MockitoBean
    private lateinit var hashService: HashService

    private val validPassword = "StrongP@ssword1234!"

    @Test
    fun `should update password successfully`() {
        val userId = 1
        val input = UpdateUserPasswordInput(password = validPassword)
        val existingUser = UserMock.create(id = userId, password = "oldHashedPassword")
        val updatedUser = UserMock.create(id = userId, password = "newHashedPassword")

        `when`(userRepository.findById(userId)).thenReturn(existingUser)
        `when`(hashService.hashBcrypt(validPassword)).thenReturn("newHashedPassword")
        `when`(userRepository.save(any())).thenReturn(updatedUser)

        val result = updateUserPassword.execute(userId, input)

        assertThat(result).isNotNull
        assertThat(result?.password).isEqualTo("newHashedPassword")
    }

    @Test
    fun `should throw BackendUsageException when password is too weak`() {
        val userId = 1
        val input = UpdateUserPasswordInput(password = "weak")

        val exception = assertThrows<BackendUsageException> {
            updateUserPassword.execute(userId, input)
        }
        assertThat(exception.code).isEqualTo(BackendUsageErrorCode.PASSWORD_TOO_WEAK_EXCEPTION)
        assertThat(exception.message).contains("password must be at least 10 characters")
    }

    @Test
    fun `should throw BackendUsageException when user not found`() {
        val userId = 999
        val input = UpdateUserPasswordInput(password = validPassword)

        `when`(userRepository.findById(userId)).thenReturn(null)

        val exception = assertThrows<BackendUsageException> {
            updateUserPassword.execute(userId, input)
        }
        assertThat(exception.code).isEqualTo(BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION)
        assertThat(exception.message).contains("user not found with id=999")
    }

    @Test
    fun `should propagate BackendInternalException from repository`() {
        val userId = 1
        val input = UpdateUserPasswordInput(password = validPassword)
        val existingUser = UserMock.create(id = userId, password = "oldHashedPassword")
        val internalException = BackendInternalException(
            message = "Repository error",
            originalException = RuntimeException("Database error")
        )

        `when`(userRepository.findById(userId)).thenReturn(existingUser)
        `when`(hashService.hashBcrypt(validPassword)).thenReturn("newHashedPassword")
        `when`(userRepository.save(any())).thenAnswer { throw internalException }

        val exception = assertThrows<BackendInternalException> {
            updateUserPassword.execute(userId, input)
        }
        assertThat(exception.message).isEqualTo("Repository error")
    }

    @Test
    fun `should preserve other user fields when updating password`() {
        val userId = 1
        val input = UpdateUserPasswordInput(password = validPassword)
        val existingUser = UserMock.create(
            id = userId,
            firstName = "John",
            lastName = "Doe",
            email = "john@example.com",
            password = "oldHashedPassword",
            serviceId = 10
        )
        val updatedUser = UserMock.create(
            id = userId,
            firstName = "john",
            lastName = "doe",
            email = "john@example.com",
            password = "newHashedPassword",
            serviceId = 10
        )

        `when`(userRepository.findById(userId)).thenReturn(existingUser)
        `when`(hashService.hashBcrypt(validPassword)).thenReturn("newHashedPassword")
        `when`(userRepository.save(any())).thenReturn(updatedUser)

        val result = updateUserPassword.execute(userId, input)

        assertThat(result).isNotNull
        assertThat(result?.email).isEqualTo("john@example.com")
        assertThat(result?.serviceId).isEqualTo(10)
    }
}
