package fr.gouv.gmampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.HashService
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.CreateUser
import fr.gouv.dgampa.rapportnav.infrastructure.api.auth.adapters.inputs.AuthRegisterDataInput
import fr.gouv.dgampa.rapportnav.infrastructure.exceptions.BackendRequestException
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

@SpringBootTest(classes = [CreateUser::class])
@ContextConfiguration(classes = [CreateUser::class])
class CreateUserTest {

    @Autowired
    private lateinit var createUser: CreateUser

    @MockitoBean
    private lateinit var userRepository: IUserRepository

    @MockitoBean
    private lateinit var hashService: HashService

    private val validPassword = "StrongP@ssword1234!"

    @Test
    fun `should create user successfully`() {
        val input = AuthRegisterDataInput(
            id = null,
            firstName = "John",
            lastName = "Doe",
            email = "john@example.com",
            password = validPassword,
            serviceId = 10,
            roles = listOf(RoleTypeEnum.USER_PAM)
        )
        val savedUser = UserMock.create(id = 1, email = "john@example.com")

        `when`(hashService.hashBcrypt(validPassword)).thenReturn("hashedPassword")
        `when`(userRepository.findByEmail("john@example.com")).thenReturn(null)
        `when`(userRepository.save(any())).thenReturn(savedUser)

        val result = createUser.execute(input)

        assertThat(result).isNotNull
        assertThat(result?.email).isEqualTo("john@example.com")
    }

    @Test
    fun `should throw BackendRequestException when required fields are empty`() {
        val input = AuthRegisterDataInput(
            id = null,
            firstName = "",
            lastName = "Doe",
            email = "john@example.com",
            password = validPassword,
            roles = null
        )

        val exception = assertThrows<BackendRequestException> {
            createUser.execute(input)
        }
        assertThat(exception.message).contains("required data")
    }

    @Test
    fun `should throw BackendUsageException when password is too weak`() {
        val input = AuthRegisterDataInput(
            id = null,
            firstName = "John",
            lastName = "Doe",
            email = "john@example.com",
            password = "weak",
            roles = null
        )

        val exception = assertThrows<BackendUsageException> {
            createUser.execute(input)
        }
        assertThat(exception.message).contains("Password must be at least 10 characters")
    }

    @Test
    fun `should throw BackendUsageException when user already exists`() {
        val input = AuthRegisterDataInput(
            id = null,
            firstName = "John",
            lastName = "Doe",
            email = "existing@example.com",
            password = validPassword,
            roles = null
        )
        val existingUser = UserMock.create(id = 1, email = "existing@example.com")

        `when`(userRepository.findByEmail("existing@example.com")).thenReturn(existingUser)

        val exception = assertThrows<BackendUsageException> {
            createUser.execute(input)
        }
        assertThat(exception.message).contains("User already exists")
    }

    @Test
    fun `should throw BackendInternalException when repository fails`() {
        val input = AuthRegisterDataInput(
            id = null,
            firstName = "John",
            lastName = "Doe",
            email = "john@example.com",
            password = validPassword,
            roles = null
        )

        `when`(hashService.hashBcrypt(validPassword)).thenReturn("hashedPassword")
        `when`(userRepository.findByEmail("john@example.com")).thenReturn(null)
        `when`(userRepository.save(any())).thenAnswer { throw RuntimeException("Database error") }

        val exception = assertThrows<BackendInternalException> {
            createUser.execute(input)
        }
        assertThat(exception.message).contains("CreateUser failed for email=john@example.com")
    }

    @Test
    fun `should use default role when roles is null`() {
        val input = AuthRegisterDataInput(
            id = null,
            firstName = "John",
            lastName = "Doe",
            email = "john@example.com",
            password = validPassword,
            serviceId = null,
            roles = null
        )
        val savedUser = UserMock.create(id = 1, roles = listOf(RoleTypeEnum.USER_PAM))

        `when`(hashService.hashBcrypt(validPassword)).thenReturn("hashedPassword")
        `when`(userRepository.findByEmail("john@example.com")).thenReturn(null)
        `when`(userRepository.save(any())).thenReturn(savedUser)

        val result = createUser.execute(input)

        assertThat(result).isNotNull
        assertThat(result?.roles).contains(RoleTypeEnum.USER_PAM)
    }
}
