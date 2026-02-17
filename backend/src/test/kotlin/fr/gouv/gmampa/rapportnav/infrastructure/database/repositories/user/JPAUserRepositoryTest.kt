package fr.gouv.gmampa.rapportnav.infrastructure.database.repositories.user

import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.user.UserModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.user.IDBUserRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.user.JPAUserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import tools.jackson.databind.json.JsonMapper
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [JPAUserRepository::class])
class JPAUserRepositoryTest {

    @MockitoBean
    private lateinit var dbUserRepository: IDBUserRepository

    @MockitoBean
    private lateinit var mapper: JsonMapper

    private lateinit var jpaUserRepository: JPAUserRepository

    private val userModel = UserModel(
        id = 1,
        firstName = "John",
        lastName = "Doe",
        email = "john.doe@example.com",
        password = "hashedPassword",
        serviceId = 1,
        roles = listOf(RoleTypeEnum.USER_PAM)
    )

    @BeforeEach
    fun setUp() {
        jpaUserRepository = JPAUserRepository(dbUserRepository, mapper)
    }

    @Test
    fun `findById should return user when found`() {
        `when`(dbUserRepository.findById(1)).thenReturn(Optional.of(userModel))

        val result = jpaUserRepository.findById(1)

        assertThat(result).isNotNull
        assertThat(result?.id).isEqualTo(1)
        assertThat(result?.email).isEqualTo("john.doe@example.com")
        verify(dbUserRepository).findById(1)
    }

    @Test
    fun `findById should return null when not found`() {
        `when`(dbUserRepository.findById(999)).thenReturn(Optional.empty())

        val result = jpaUserRepository.findById(999)

        assertThat(result).isNull()
        verify(dbUserRepository).findById(999)
    }

    @Test
    fun `findById should throw BackendInternalException on error`() {
        `when`(dbUserRepository.findById(1)).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaUserRepository.findById(1)
        }
        assertThat(exception.message).contains("findById failed")
    }

    @Test
    fun `findByEmail should return user when found`() {
        `when`(dbUserRepository.findByEmail("john.doe@example.com")).thenReturn(userModel)

        val result = jpaUserRepository.findByEmail("john.doe@example.com")

        assertThat(result).isNotNull
        assertThat(result?.email).isEqualTo("john.doe@example.com")
        verify(dbUserRepository).findByEmail("john.doe@example.com")
    }

    @Test
    fun `findByEmail should return null when not found`() {
        `when`(dbUserRepository.findByEmail("unknown@example.com")).thenReturn(null)

        val result = jpaUserRepository.findByEmail("unknown@example.com")

        assertThat(result).isNull()
        verify(dbUserRepository).findByEmail("unknown@example.com")
    }

    @Test
    fun `findByEmail should throw BackendInternalException on error`() {
        `when`(dbUserRepository.findByEmail("john.doe@example.com")).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaUserRepository.findByEmail("john.doe@example.com")
        }
        assertThat(exception.message).contains("findByEmail failed")
    }

    @Test
    fun `findAll should return list of users`() {
        `when`(dbUserRepository.findAll()).thenReturn(listOf(userModel))

        val result = jpaUserRepository.findAll()

        assertThat(result).hasSize(1)
        assertThat(result[0].email).isEqualTo("john.doe@example.com")
        verify(dbUserRepository).findAll()
    }

    @Test
    fun `findAll should return empty list when no users`() {
        `when`(dbUserRepository.findAll()).thenReturn(emptyList())

        val result = jpaUserRepository.findAll()

        assertThat(result).isEmpty()
        verify(dbUserRepository).findAll()
    }

    @Test
    fun `findAll should throw BackendInternalException on error`() {
        `when`(dbUserRepository.findAll()).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaUserRepository.findAll()
        }
        assertThat(exception.message).contains("findAll failed")
    }

    @Test
    fun `save should return saved user`() {
        val user = User(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            password = "hashedPassword",
            serviceId = 1,
            roles = listOf(RoleTypeEnum.USER_PAM)
        )

        `when`(dbUserRepository.save(any<UserModel>())).thenReturn(userModel)

        val result = jpaUserRepository.save(user)

        assertThat(result).isNotNull
        assertThat(result?.email).isEqualTo("john.doe@example.com")
        verify(dbUserRepository).save(any<UserModel>())
    }

    @Test
    fun `save should throw BackendUsageException on invalid data`() {
        val user = User(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            password = "hashedPassword",
            roles = listOf(RoleTypeEnum.USER_PAM)
        )

        `when`(dbUserRepository.save(any<UserModel>())).thenThrow(InvalidDataAccessApiUsageException("Invalid data"))

        val exception = assertThrows<BackendUsageException> {
            jpaUserRepository.save(user)
        }
        assertThat(exception.message).contains("invalid data")
    }

    @Test
    fun `save should throw BackendInternalException on database error`() {
        val user = User(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            password = "hashedPassword",
            roles = listOf(RoleTypeEnum.USER_PAM)
        )

        `when`(dbUserRepository.save(any<UserModel>())).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaUserRepository.save(user)
        }
        assertThat(exception.message).contains("save failed")
    }

    @Test
    fun `disable should set disabledAt and return user`() {
        val userModelCopy = UserModel(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            password = "hashedPassword",
            serviceId = 1,
            roles = listOf(RoleTypeEnum.USER_PAM),
            disabledAt = null
        )

        `when`(dbUserRepository.findById(1)).thenReturn(Optional.of(userModelCopy))
        `when`(dbUserRepository.save(any<UserModel>())).thenAnswer { invocation ->
            invocation.getArgument<UserModel>(0)
        }

        val result = jpaUserRepository.disable(1)

        assertThat(result).isNotNull
        assertThat(result?.disabledAt).isNotNull
        verify(dbUserRepository).findById(1)
        verify(dbUserRepository).save(any<UserModel>())
    }

    @Test
    fun `disable should return null when user not found`() {
        `when`(dbUserRepository.findById(999)).thenReturn(Optional.empty())

        val result = jpaUserRepository.disable(999)

        assertThat(result).isNull()
        verify(dbUserRepository).findById(999)
    }

    @Test
    fun `disable should throw BackendInternalException on error`() {
        `when`(dbUserRepository.findById(1)).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaUserRepository.disable(1)
        }
        assertThat(exception.message).contains("disable failed")
    }

    @Test
    fun `enable should set disabledAt to null and return user`() {
        val disabledUserModel = UserModel(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            email = "john.doe@example.com",
            password = "hashedPassword",
            serviceId = 1,
            roles = listOf(RoleTypeEnum.USER_PAM),
            disabledAt = java.time.Instant.now()
        )

        `when`(dbUserRepository.findById(1)).thenReturn(Optional.of(disabledUserModel))
        `when`(dbUserRepository.save(any<UserModel>())).thenAnswer { invocation ->
            invocation.getArgument<UserModel>(0)
        }

        val result = jpaUserRepository.enable(1)

        assertThat(result).isNotNull
        assertThat(result?.disabledAt).isNull()
        verify(dbUserRepository).findById(1)
        verify(dbUserRepository).save(any<UserModel>())
    }

    @Test
    fun `enable should return null when user not found`() {
        `when`(dbUserRepository.findById(999)).thenReturn(Optional.empty())

        val result = jpaUserRepository.enable(999)

        assertThat(result).isNull()
        verify(dbUserRepository).findById(999)
    }

    @Test
    fun `enable should throw BackendInternalException on error`() {
        `when`(dbUserRepository.findById(1)).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            jpaUserRepository.enable(1)
        }
        assertThat(exception.message).contains("enable failed")
    }
}
