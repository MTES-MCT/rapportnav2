package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.DeleteEnvMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.DeleteMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.DeleteNavMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetNavMissionById
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionNavEntityMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*

@SpringBootTest(classes = [DeleteMission::class])
class DeleteMissionTest {

    @Autowired
    private lateinit var deleteMission: DeleteMission

    @MockitoBean
    private lateinit var getNavMissionById: GetNavMissionById

    @MockitoBean
    private lateinit var deleteNavMission: DeleteNavMission

    @MockitoBean
    private lateinit var deleteEnvMission: DeleteEnvMission

    @MockitoBean
    private lateinit var missionNavRepository: IMissionNavRepository

    @Test
    fun `should throw exception when id is null`() {
        val exception = assertThrows<BackendUsageException> {
            deleteMission.execute(id = null, serviceId = null)
        }
        assertEquals(BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION, exception.code)
    }

    @Test
    fun `should throw exception when nav mission not found`() {
        val id = UUID.randomUUID()
        `when`(getNavMissionById.execute(id = id)).thenReturn(null)

        val exception = assertThrows<BackendUsageException> {
            deleteMission.execute(id = id, serviceId = 1)
        }
        assertEquals(BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION, exception.code)
        verifyNoInteractions(deleteNavMission)
        verifyNoInteractions(deleteEnvMission)
    }

    @Test
    fun `should delete the MonitorEnv mission and the local mirror row when externalId exists`() {
        val id = UUID.randomUUID()
        val serviceId = 5
        val navMission = MissionNavEntityMock.create(id = id, externalId = "123", serviceId = serviceId)

        `when`(getNavMissionById.execute(id = id)).thenReturn(navMission)

        deleteMission.execute(id = id, serviceId = serviceId)

        verify(deleteEnvMission).execute(id = 123, serviceId = serviceId)
        verify(missionNavRepository).deleteById(id)
        // env-mirror rows must not go through DeleteNavMission's RAPPORT_NAV/service guards
        verifyNoInteractions(deleteNavMission)
    }

    @Test
    fun `should delete only the nav mission when externalId is null`() {
        val id = UUID.randomUUID()
        val serviceId = 5
        val navMission = MissionNavEntityMock.create(id = id, externalId = null, serviceId = serviceId)

        `when`(getNavMissionById.execute(id = id)).thenReturn(navMission)

        deleteMission.execute(id = id, serviceId = serviceId)

        verifyNoInteractions(deleteEnvMission)
        verify(missionNavRepository, never()).deleteById(id)
        verify(deleteNavMission).execute(id = id, serviceId = serviceId)
    }

    @Test
    fun `should not delete the mirror row when deleteEnvMission throws`() {
        val id = UUID.randomUUID()
        val serviceId = 5
        val navMission = MissionNavEntityMock.create(id = id, externalId = "123", serviceId = serviceId)

        `when`(getNavMissionById.execute(id = id)).thenReturn(navMission)
        doThrow(
            BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_DELETE_EXCEPTION,
                message = "DeleteEnvMission: cannot delete mission with existing actions"
            )
        ).`when`(deleteEnvMission).execute(id = 123, serviceId = serviceId)

        assertThrows<BackendUsageException> {
            deleteMission.execute(id = id, serviceId = serviceId)
        }

        verify(missionNavRepository, never()).deleteById(id)
        verifyNoInteractions(deleteNavMission)
    }
}
