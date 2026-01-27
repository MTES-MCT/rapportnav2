package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.*
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.DeleteNavMission
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionNavActionEntityMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*


@SpringBootTest(classes = [DeleteNavMission::class])
@ContextConfiguration(classes = [DeleteNavMission::class])
class DeleteNavMissionTest {

    @Autowired
    private lateinit var deleteNavMission: DeleteNavMission

    @MockitoBean
    private lateinit var missionRepo: IMissionNavRepository

    @MockitoBean
    private lateinit var getNavActionListByOwnerId: GetNavActionListByOwnerId

    @MockitoBean
    private lateinit var deleteNavAction: DeleteNavAction

    @Test
    fun `should throw exception when id is null`() {
        val exception = assertThrows<BackendUsageException> {
            deleteNavMission.execute(id = null)
        }
        assertEquals(BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION, exception.code)
        assertEquals("DeleteNavMission: mission id is required", exception.message)
    }

    @Test
    fun `should throw exception when mission not found`() {
        val id = UUID.randomUUID()
        `when`(missionRepo.finById(id = id)).thenReturn(Optional.empty())

        val exception = assertThrows<BackendUsageException> {
            deleteNavMission.execute(id = id, serviceId = 1)
        }
        assertEquals(BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION, exception.code)
    }

    @Test
    fun `should throw exception if service Id is not the same`() {
        val serviceId = 5
        val ownerId = UUID.randomUUID()

        val model = MissionModel(
            id = ownerId,
            serviceId = serviceId,
            startDateTimeUtc = Instant.now(),
            missionSource = MissionSourceEnum.RAPPORTNAV
        )

        `when`(missionRepo.finById(id = ownerId)).thenReturn(Optional.of(model))

        val exception = assertThrows<BackendUsageException> {
            deleteNavMission.execute(id = ownerId, serviceId = 4)
        }
        assertEquals(BackendUsageErrorCode.COULD_NOT_DELETE_EXCEPTION, exception.code)
        assertEquals("DeleteNavMission: mission does not belong to this service", exception.message)
        verify(missionRepo, times(0)).deleteById(ownerId)
    }

    @Test
    fun `should throw exception if source is not rapportNav`() {
        val serviceId = 5
        val missionSource = MissionSourceEnum.MONITORENV
        val ownerId = UUID.randomUUID()

        val model = MissionModel(
            id = ownerId,
            serviceId = serviceId,
            startDateTimeUtc = Instant.now(),
            missionSource = missionSource
        )

        `when`(missionRepo.finById(id = ownerId)).thenReturn(Optional.of(model))

        val exception = assertThrows<BackendUsageException> {
            deleteNavMission.execute(id = ownerId, serviceId = serviceId)
        }
        assertEquals(BackendUsageErrorCode.COULD_NOT_DELETE_EXCEPTION, exception.code)
        assertEquals("DeleteNavMission: mission source must be RAPPORT_NAV", exception.message)
        verify(missionRepo, times(0)).deleteById(ownerId)
    }

    @Test
    fun `should call delete of repository`() {
        val serviceId = 5
        val ownerId = UUID.randomUUID()
        val action = MissionNavActionEntityMock.create(id = UUID.randomUUID(), actionType = ActionType.INQUIRY)

        val model = MissionModel(
            id = ownerId,
            serviceId = serviceId,
            startDateTimeUtc = Instant.now(),
            missionSource = MissionSourceEnum.RAPPORTNAV
        )

        `when`(missionRepo.finById(id = ownerId)).thenReturn(Optional.of(model))
        `when`(getNavActionListByOwnerId.execute(ownerId = ownerId)).thenReturn(listOf(action))

        deleteNavMission.execute(id = ownerId, serviceId = serviceId)
        verify(missionRepo, times(1)).deleteById(ownerId)
        verify(deleteNavAction, times(1)).execute(id = action.id)
    }

    @Test
    fun `should delete mission with RAPPORT_NAV source`() {
        val serviceId = 5
        val ownerId = UUID.randomUUID()

        val model = MissionModel(
            id = ownerId,
            serviceId = serviceId,
            startDateTimeUtc = Instant.now(),
            missionSource = MissionSourceEnum.RAPPORT_NAV
        )

        `when`(missionRepo.finById(id = ownerId)).thenReturn(Optional.of(model))
        `when`(getNavActionListByOwnerId.execute(ownerId = ownerId)).thenReturn(emptyList())

        deleteNavMission.execute(id = ownerId, serviceId = serviceId)
        verify(missionRepo, times(1)).deleteById(ownerId)
    }
}
