package fr.gouv.gmampa.rapportnav.infrastructure.api.admin

import fr.gouv.dgampa.rapportnav.RapportNavApplication
import fr.gouv.dgampa.rapportnav.config.ApiKeyAuthenticationFilter
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.GetAuthenticationAuditList
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.*
import fr.gouv.dgampa.rapportnav.infrastructure.api.admin.UserAdminController
import fr.gouv.gmampa.rapportnav.mocks.user.UserMock
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.cache.autoconfigure.CacheAutoConfiguration
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum
import fr.gouv.dgampa.rapportnav.infrastructure.api.auth.adapters.inputs.AuthRegisterDataInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.auth.adapters.inputs.UpdateUserInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.auth.adapters.inputs.UpdateUserPasswordInput
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import tools.jackson.databind.json.JsonMapper
import java.time.Instant

@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = [RapportNavApplication::class])
@WebMvcTest(UserAdminController::class)
@ImportAutoConfiguration(CacheAutoConfiguration::class)
class UserAdminControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var updateUser: UpdateUser

    @MockitoBean
    private lateinit var disableUser: DisableUser

    @MockitoBean
    private lateinit var enableUser: EnableUser

    @MockitoBean
    private lateinit var getUserList: GetUserList

    @MockitoBean
    private lateinit var createUser: CreateUser

    @MockitoBean
    private lateinit var updatePassword: UpdateUserPassword

    @MockitoBean
    private lateinit var tokenService: TokenService

    @MockitoBean
    private lateinit var apiKeyAuthenticationFilter: ApiKeyAuthenticationFilter

    @MockitoBean
    private lateinit var getAuthenticationAuditList: GetAuthenticationAuditList

    private val jsonMapper = JsonMapper.builder().build()

    @Test
    fun `getServices should return list of users`() {
        val users = listOf(
            UserMock.create(id = 1, email = "user1@example.com", firstName = "John", lastName = "Doe"),
            UserMock.create(id = 2, email = "user2@example.com", firstName = "Jane", lastName = "Smith")
        )

        `when`(getUserList.execute()).thenReturn(users)

        mockMvc.perform(get("/api/v2/admin/users"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].email").value("user1@example.com"))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].email").value("user2@example.com"))
    }

    @Test
    fun `getServices should return empty list when no users`() {
        `when`(getUserList.execute()).thenReturn(emptyList())

        mockMvc.perform(get("/api/v2/admin/users"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(0))
    }

    @Test
    fun `create should return created user`() {
        val input = AuthRegisterDataInput(
            id = null,
            firstName = "John",
            lastName = "Doe",
            email = "john@example.com",
            password = "StrongP@ssword123!",
            serviceId = 10,
            roles = listOf(RoleTypeEnum.USER_PAM)
        )
        val createdUser = UserMock.create(
            id = 1,
            firstName = "John",
            lastName = "Doe",
            email = "john@example.com",
            serviceId = 10,
            roles = listOf(RoleTypeEnum.USER_PAM)
        )

        `when`(createUser.execute(org.mockito.kotlin.any())).thenReturn(createdUser)

        mockMvc.perform(
            post("/api/v2/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(input))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.email").value("john@example.com"))
            .andExpect(jsonPath("$.firstName").value("John"))
            .andExpect(jsonPath("$.lastName").value("Doe"))
    }

    @Test
    fun `create should return null when creation fails`() {
        val input = AuthRegisterDataInput(
            id = null,
            firstName = "John",
            lastName = "Doe",
            email = "john@example.com",
            password = "StrongP@ssword123!",
            serviceId = null,
            roles = null
        )

        `when`(createUser.execute(org.mockito.kotlin.any())).thenReturn(null)

        mockMvc.perform(
            post("/api/v2/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(input))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").doesNotExist())
    }

    @Test
    fun `update should return updated user`() {
        val input = UpdateUserInput(
            id = 1,
            firstName = "John",
            lastName = "Updated",
            email = "john@example.com",
            serviceId = 10,
            roles = listOf(RoleTypeEnum.USER_PAM)
        )
        val updatedUser = UserMock.create(
            id = 1,
            firstName = "John",
            lastName = "Updated",
            email = "john@example.com",
            serviceId = 10,
            roles = listOf(RoleTypeEnum.USER_PAM)
        )

        `when`(updateUser.execute(org.mockito.kotlin.any())).thenReturn(updatedUser)

        mockMvc.perform(
            put("/api/v2/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(input))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.lastName").value("Updated"))
    }

    @Test
    fun `update should return null when user not found`() {
        val input = UpdateUserInput(
            id = 999,
            firstName = "John",
            lastName = "Doe",
            email = "john@example.com",
            serviceId = null,
            roles = null
        )

        `when`(updateUser.execute(org.mockito.kotlin.any())).thenReturn(null)

        mockMvc.perform(
            put("/api/v2/admin/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(input))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").doesNotExist())
    }

    @Test
    fun `updatePassword should return updated user`() {
        val input = UpdateUserPasswordInput(password = "NewStrongP@ssword123!")
        val updatedUser = UserMock.create(id = 1, email = "john@example.com")

        `when`(updatePassword.execute(org.mockito.kotlin.eq(1), org.mockito.kotlin.any())).thenReturn(updatedUser)

        mockMvc.perform(
            post("/api/v2/admin/users/password/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(input))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.email").value("john@example.com"))
    }

    @Test
    fun `updatePassword should return null when user not found`() {
        val input = UpdateUserPasswordInput(password = "NewStrongP@ssword123!")

        `when`(updatePassword.execute(org.mockito.kotlin.eq(999), org.mockito.kotlin.any())).thenReturn(null)

        mockMvc.perform(
            post("/api/v2/admin/users/password/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(input))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").doesNotExist())
    }

    @Test
    fun `disable should return disabled user when user exists`() {
        val user = UserMock.create(id = 1, email = "test@example.com", disabledAt = Instant.now())

        `when`(disableUser.execute(1)).thenReturn(user)

        mockMvc.perform(post("/api/v2/admin/users/1/disable"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.email").value("test@example.com"))
    }

    @Test
    fun `disable should return null when user not found`() {
        `when`(disableUser.execute(999)).thenReturn(null)

        mockMvc.perform(post("/api/v2/admin/users/999/disable"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").doesNotExist())
    }

    @Test
    fun `enable should return enabled user when user exists`() {
        val user = UserMock.create(id = 1, email = "test@example.com", disabledAt = null)

        `when`(enableUser.execute(1)).thenReturn(user)

        mockMvc.perform(post("/api/v2/admin/users/1/enable"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.email").value("test@example.com"))
    }

    @Test
    fun `enable should return null when user not found`() {
        `when`(enableUser.execute(999)).thenReturn(null)

        mockMvc.perform(post("/api/v2/admin/users/999/enable"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").doesNotExist())
    }
}