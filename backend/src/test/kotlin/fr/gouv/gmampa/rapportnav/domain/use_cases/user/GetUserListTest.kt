package fr.gouv.gmampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.user.IUserRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetUserList
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.user.UserModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [GetUserList::class])
@ContextConfiguration(classes = [GetUserList::class])
class GetUserListTest {

    @Autowired
    private lateinit var getUserList: GetUserList

    @MockitoBean
    private lateinit var userRepository: IUserRepository

    @Test
    fun `should return list of users`() {
        val userModels = listOf(
            UserModel(
                id = 1,
                firstName = "John",
                lastName = "Doe",
                email = "john@example.com",
                password = "hashedpassword"
            ),
            UserModel(
                id = 2,
                firstName = "Jane",
                lastName = "Smith",
                email = "jane@example.com",
                password = "hashedpassword"
            )
        )

        `when`(userRepository.findAll()).thenReturn(userModels)

        val result = getUserList.execute()

        assertThat(result).hasSize(2)
        assertThat(result[0].email).isEqualTo("john@example.com")
        assertThat(result[1].email).isEqualTo("jane@example.com")
    }

    @Test
    fun `should return empty list when no users`() {
        `when`(userRepository.findAll()).thenReturn(emptyList())

        val result = getUserList.execute()

        assertThat(result).isEmpty()
    }

    @Test
    fun `should throw BackendInternalException when repository fails`() {
        `when`(userRepository.findAll()).thenAnswer { throw RuntimeException("Database error") }

        val exception = assertThrows<BackendInternalException> {
            getUserList.execute()
        }
        assertThat(exception.message).contains("GetUserList failed")
    }
}
