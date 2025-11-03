package fr.gouv.gmampa.rapportnav.infrastructure.api.auth

import fr.gouv.dgampa.rapportnav.domain.entities.user.RoleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.HashService
import fr.gouv.dgampa.rapportnav.domain.use_cases.auth.TokenService
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.FindByEmail
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.Save
import fr.gouv.dgampa.rapportnav.infrastructure.api.auth.ApiAuthController
import fr.gouv.dgampa.rapportnav.infrastructure.api.auth.adapters.inputs.AuthLoginDataInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.auth.adapters.inputs.AuthRegisterDataInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.auth.adapters.outputs.AuthLoginDataOutput
import fr.gouv.dgampa.rapportnav.infrastructure.exceptions.BackendRequestErrorCode
import fr.gouv.dgampa.rapportnav.infrastructure.exceptions.BackendRequestException
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlin.jvm.java

class ApiAuthControllerTests {
    @Mock
    private lateinit var save: Save

    @Mock
    private lateinit var findByEmail: FindByEmail

    @Mock
    private lateinit var hashService: HashService

    @Mock
    private lateinit var tokenService: TokenService

    @InjectMocks
    private lateinit var controller: ApiAuthController

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    // ---------- REGISTER TESTS ----------

    @Test
    fun `register throws exception when missing fields`() {
        val input = AuthRegisterDataInput(null, "", "John", "Doe", "f", null, null)

        val ex = assertThrows(BackendRequestException::class.java) {
            controller.register(input)
        }

        assertEquals(BackendRequestErrorCode.BODY_MISSING_DATA, ex.code)
    }

    /*TODO update when password pattern is OK
    @Test
    fun `register throws when password too weak`() {
        val input = AuthRegisterDataInput(
            id = null,
            email = "user@example.com",
            password = "weak",
            firstName = "John",
            lastName = "Doe",
            serviceId = null,
            roles = null
        )

        `when`(findByEmail.execute(anyString())).thenReturn(null)

        val ex = assertThrows(BackendUsageException::class.java) {
            controller.register(input)
        }

        assertEquals(BackendUsageErrorCode.PASSWORD_TOO_WEAK_EXCEPTION, ex.code)
    }*/

    @Test
    fun `register throws when email already exists`() {
        val input = AuthRegisterDataInput(
            id = null,
            email = "user@example.com",
            password = "StrongPassword1!!!",
            firstName = "John",
            lastName = "Doe",
            serviceId = null,
            roles = null
        )

        `when`(findByEmail.execute("user@example.com")).thenReturn(mock(User::class.java))
        `when`(hashService.hashBcrypt(anyString())).thenReturn("hashed")

        val ex = assertThrows(BackendUsageException::class.java) {
            controller.register(input)
        }

        assertEquals(BackendUsageErrorCode.ALREADY_EXISTS_EXCEPTION, ex.code)
    }

    // ---------- LOGIN TESTS ----------

    @Test
    fun `login throws when missing fields`() {
        val input = AuthLoginDataInput("", "")
        val response: HttpServletResponse = mock(HttpServletResponse::class.java)

        val ex = assertThrows(BackendRequestException::class.java) {
            controller.login(input, response)
        }

        assertEquals(BackendRequestErrorCode.BODY_MISSING_DATA, ex.code)
    }

    @Test
    fun `login throws when email not found`() {
        val input = AuthLoginDataInput("user@example.com", "password")

        val response: HttpServletResponse = mock(HttpServletResponse::class.java)

        `when`(findByEmail.execute("user@example.com")).thenReturn(null)

        val ex = assertThrows(BackendUsageException::class.java) {
            controller.login(input, response)
        }

        assertEquals(BackendUsageErrorCode.INCORRECT_USER_IDENTIFIER_EXCEPTION, ex.code)
    }

    @Test
    fun `login throws when password incorrect`() {
        val input = AuthLoginDataInput("user@example.com", "wrongpassword")
        val response: HttpServletResponse = mock(HttpServletResponse::class.java)

        val user = User(
            id = null,
            firstName = "john",
            lastName = "doe",
            email = "user@example.com",
            password = "hashed",
            serviceId = null,
            roles = listOf(RoleTypeEnum.USER_PAM)
        )

        `when`(findByEmail.execute("user@example.com")).thenReturn(user)
        `when`(hashService.checkBcrypt("wrongpassword", "hashed")).thenReturn(false)

        val ex = assertThrows(BackendUsageException::class.java) {
            controller.login(input, response)
        }

        assertEquals(BackendUsageErrorCode.INCORRECT_USER_IDENTIFIER_EXCEPTION, ex.code)
    }

    @Test
    fun `login succeeds with valid credentials`() {
        val input = AuthLoginDataInput("user@example.com", "StrongPassword1!!!")
        val response: HttpServletResponse = mock(HttpServletResponse::class.java)

        val user = User(
            id = null,
            firstName = "john",
            lastName = "doe",
            email = "user@example.com",
            password = "hashed",
            serviceId = null,
            roles = listOf(RoleTypeEnum.USER_PAM)
        )

        `when`(findByEmail.execute("user@example.com")).thenReturn(user)
        `when`(hashService.checkBcrypt("StrongPassword1!!!", "hashed")).thenReturn(true)
        `when`(tokenService.createToken(user)).thenReturn("jwt_token")

        val result = controller.login(input, response)

        assertEquals(AuthLoginDataOutput(token = "jwt_token"), result)
    }
}
