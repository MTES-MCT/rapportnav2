package fr.gouv.gmampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.UpdateUser
import fr.gouv.dgampa.rapportnav.infrastructure.api.auth.adapters.inputs.UpdateUserInput
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

@SpringBootTest(classes = [UpdateUser::class])
@ContextConfiguration(classes = [UpdateUser::class])
class UpdateUserTest {

    @Autowired
    private lateinit var updateUser: UpdateUser

    @MockitoBean
    private lateinit var userRepository: IUserRepository

    @Test
    fun `should update user successfully`() {
        val input = UpdateUserInput(
            id = 1,
            firstName = "UpdatedJohn",
            lastName = "UpdatedDoe",
            email = "updated@example.com",
            serviceId = 20,
            roles = listOf(RoleTypeEnum.USER_ULAM)
        )
        val existingUser = UserMock.create(id = 1, password = "hashedPassword")
        val updatedUser = UserMock.create(
            id = 1,
            firstName = "updatedjohn",
            lastName = "updateddoe",
            email = "updated@example.com"
        )

        `when`(userRepository.findById(1)).thenReturn(existingUser)
        `when`(userRepository.save(any())).thenReturn(updatedUser)

        val result = updateUser.execute(input)

        assertThat(result).isNotNull
        assertThat(result?.email).isEqualTo("updated@example.com")
    }

    @Test
    fun `should throw BackendUsageException when user not found`() {
        val input = UpdateUserInput(
            id = 999,
            firstName = "John",
            lastName = "Doe",
            email = "john@example.com",
            roles = null
        )

        `when`(userRepository.findById(999)).thenReturn(null)

        val exception = assertThrows<BackendUsageException> {
            updateUser.execute(input)
        }
        assertThat(exception.message).contains("User not found with id=999")
    }

    @Test
    fun `should throw BackendInternalException when repository fails on save`() {
        val input = UpdateUserInput(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            email = "john@example.com",
            roles = null
        )
        val existingUser = UserMock.create(id = 1, password = "hashedPassword")

        `when`(userRepository.findById(1)).thenReturn(existingUser)
        `when`(userRepository.save(any())).thenAnswer { throw RuntimeException("Database error") }

        val exception = assertThrows<BackendInternalException> {
            updateUser.execute(input)
        }
        assertThat(exception.message).contains("UpdateUser failed for userId=1")
    }

    @Test
    fun `should use default role when roles is null`() {
        val input = UpdateUserInput(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            email = "john@example.com",
            serviceId = null,
            roles = null
        )
        val existingUser = UserMock.create(id = 1, password = "hashedPassword")
        val updatedUser = UserMock.create(id = 1, roles = listOf(RoleTypeEnum.USER_PAM))

        `when`(userRepository.findById(1)).thenReturn(existingUser)
        `when`(userRepository.save(any())).thenReturn(updatedUser)

        val result = updateUser.execute(input)

        assertThat(result).isNotNull
        assertThat(result?.roles).contains(RoleTypeEnum.USER_PAM)
    }

    @Test
    fun `should preserve existing password`() {
        val input = UpdateUserInput(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            email = "john@example.com",
            roles = listOf(RoleTypeEnum.USER_PAM)
        )
        val existingUser = UserMock.create(id = 1, password = "existingHashedPassword")
        val updatedUser = UserMock.create(id = 1, password = "existingHashedPassword")

        `when`(userRepository.findById(1)).thenReturn(existingUser)
        `when`(userRepository.save(any())).thenReturn(updatedUser)

        val result = updateUser.execute(input)

        assertThat(result).isNotNull
        assertThat(result?.password).isEqualTo("existingHashedPassword")
    }
}
