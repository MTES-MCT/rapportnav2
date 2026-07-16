package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetEnvMissionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetServiceByControlUnit
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.DeleteEnvMission
import fr.gouv.gmampa.rapportnav.mocks.mission.EnvMissionMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.EnvActionControlMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean


@SpringBootTest(classes = [DeleteEnvMission::class])
@ContextConfiguration(classes = [DeleteEnvMission::class])
class DeleteEnvMissionTest {

    @Autowired
    private lateinit var deleteEnvMission: DeleteEnvMission

    @MockitoBean
    private lateinit var missionRepo: IEnvMissionRepository

    @MockitoBean
    private lateinit var getEnvMissionById: GetEnvMissionById

    @MockitoBean
    private lateinit var getServiceByControlUnit: GetServiceByControlUnit

    @Test
    fun `should throw exception when id is null`() {
        val exception = assertThrows<BackendUsageException> {
            deleteEnvMission.execute(id = null, serviceId = null)
        }
        assertEquals(BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION, exception.code)
        assertEquals("DeleteEnvMission: mission id is required", exception.message)
    }

    @Test
    fun `should throw exception when mission not found`() {
        val id = 1
        `when`(getEnvMissionById.execute(id)).thenReturn(null)

        val exception = assertThrows<BackendUsageException> {
            deleteEnvMission.execute(id = id, serviceId = 1)
        }
        assertEquals(BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION, exception.code)
    }

    @Test
    fun `should throw exception if service Id is not the same`() {
        val id = 1
        val serviceId = 5
        val service = ServiceEntity(
            id = 3,
            name = "service",
            serviceType = ServiceTypeEnum.ULAM
        )
        val mission = EnvMissionMock.create(id = id)

        `when`(getEnvMissionById.execute(id)).thenReturn(mission)
        `when`(getServiceByControlUnit.execute(any())).thenReturn(listOf(service))

        val exception = assertThrows<BackendUsageException> {
            deleteEnvMission.execute(id = id, serviceId = serviceId)
        }
        assertEquals(BackendUsageErrorCode.COULD_NOT_DELETE_EXCEPTION, exception.code)
        assertEquals("DeleteEnvMission: mission does not belong to this service", exception.message)
        verify(missionRepo, times(0)).deleteMission(id, source = mission.missionSource)
    }

    @Test
    fun `should throw exception if env mission has actions`() {
        val id = 1
        val serviceId = 3
        val service = ServiceEntity(
            id = serviceId,
            name = "service",
            serviceType = ServiceTypeEnum.ULAM
        )
        val mission = EnvMissionMock.create(
            id = id,
            envActions = listOf(EnvActionControlMock.create())
        )

        `when`(getEnvMissionById.execute(id)).thenReturn(mission)
        `when`(getServiceByControlUnit.execute(any())).thenReturn(listOf(service))

        val exception = assertThrows<BackendUsageException> {
            deleteEnvMission.execute(id = id, serviceId = serviceId)
        }
        assertEquals(BackendUsageErrorCode.COULD_NOT_DELETE_EXCEPTION, exception.code)
        assertEquals("DeleteEnvMission: cannot delete mission with existing actions", exception.message)
        verify(missionRepo, times(0)).deleteMission(id, source = mission.missionSource)
    }

    @Test
    fun `should call delete of repository`() {
        val id = 1
        val service = ServiceEntity(
            id = 3,
            name = "service",
            serviceType = ServiceTypeEnum.ULAM
        )
        val mission = EnvMissionMock.create(id = id)

        `when`(getEnvMissionById.execute(id)).thenReturn(mission)
        `when`(getServiceByControlUnit.execute(any())).thenReturn(listOf(service))

        deleteEnvMission.execute(id = id, serviceId = service.id)
        verify(getEnvMissionById, times(1)).execute(id)
        verify(missionRepo, times(1)).deleteMission(id, source = mission.missionSource)
    }
}
