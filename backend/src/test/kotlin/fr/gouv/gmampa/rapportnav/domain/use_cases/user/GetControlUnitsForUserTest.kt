package fr.gouv.gmampa.rapportnav.domain.use_cases.user

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetControlUnitsForUser
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetUserFromToken
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import fr.gouv.gmampa.rapportnav.mocks.user.UserMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [GetControlUnitsForUser::class])
@ContextConfiguration(classes = [GetControlUnitsForUser::class])
class GetControlUnitsForUserTest {

    @Autowired
    private lateinit var getControlUnitsForUser: GetControlUnitsForUser

    @MockitoBean
    private lateinit var getUserFromToken: GetUserFromToken

    @MockitoBean
    private lateinit var getServiceById: GetServiceById

    @Test
    fun `should return control units for user`() {
        val user = UserMock.create(id = 1, serviceId = 10)
        val service = ServiceEntityMock.create(id = 10, controlUnits = listOf(100, 200))

        `when`(getUserFromToken.execute()).thenReturn(user)
        `when`(getServiceById.execute(10)).thenReturn(service)

        val result = getControlUnitsForUser.execute()

        assertThat(result).containsExactly(100, 200)
    }

    @Test
    fun `should return null when user not found`() {
        `when`(getUserFromToken.execute()).thenReturn(null)

        val result = getControlUnitsForUser.execute()

        assertThat(result).isNull()
    }

    @Test
    fun `should return null when service not found`() {
        val user = UserMock.create(id = 1, serviceId = 10)

        `when`(getUserFromToken.execute()).thenReturn(user)
        `when`(getServiceById.execute(10)).thenReturn(null)

        val result = getControlUnitsForUser.execute()

        assertThat(result).isNull()
    }

    @Test
    fun `should return null when user has no serviceId`() {
        val user = UserMock.create(id = 1, serviceId = null)

        `when`(getUserFromToken.execute()).thenReturn(user)
        `when`(getServiceById.execute(null)).thenReturn(null)

        val result = getControlUnitsForUser.execute()

        assertThat(result).isNull()
    }

    @Test
    fun `should throw BackendInternalException when dependency fails`() {
        `when`(getUserFromToken.execute()).thenAnswer { throw RuntimeException("Error") }

        val exception = assertThrows<BackendInternalException> {
            getControlUnitsForUser.execute()
        }
        assertThat(exception.message).contains("GetControlUnitsForUser failed")
    }

    @Test
    fun `should propagate BackendInternalException from getServiceById`() {
        val user = UserMock.create(id = 1, serviceId = 10)
        val originalException = BackendInternalException(
            message = "GetServiceById failed",
            originalException = RuntimeException("Cause")
        )

        `when`(getUserFromToken.execute()).thenReturn(user)
        `when`(getServiceById.execute(10)).thenAnswer { throw originalException }

        val exception = assertThrows<BackendInternalException> {
            getControlUnitsForUser.execute()
        }
        assertThat(exception.message).isEqualTo("GetServiceById failed")
    }
}
