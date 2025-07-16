package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.*
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.DeleteMissionNav
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionNavActionEntityMock
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*


@SpringBootTest(classes = [DeleteMissionNav::class])
@ContextConfiguration(classes = [DeleteMissionNav::class])
class DeleteMissionNavTest {

    @Autowired
    private lateinit var deleteMissionNav: DeleteMissionNav

    @MockitoBean
    private lateinit var missionRepo: IMissionNavRepository

    @MockitoBean
    private lateinit var getNavActionListByOwnerId: GetNavActionListByOwnerId

    @MockitoBean
    private lateinit var deleteNavAction: DeleteNavAction

    @Test
    fun `should not call repository when id is null`() {
        val id = UUID.randomUUID()
        deleteMissionNav.execute(id  = null)
        verify(missionRepo, times(0)).deleteById(id)
    }

    @Test
    fun `should not call delete if service Id is not the same `() {
        val serviceId = 5
        val ownerId = UUID.randomUUID()
        val action = MissionNavActionEntityMock.create(id = UUID.randomUUID(), actionType = ActionType.INQUIRY)

        val  model = MissionModel(
            id = ownerId,
            serviceId = serviceId,
            startDateTimeUtc = Instant.now(),
            missionSource = MissionSourceEnum.RAPPORTNAV
        )

        `when`(missionRepo.finById(id = ownerId)).thenReturn(Optional.of(model))
        `when`(getNavActionListByOwnerId.execute(ownerId = ownerId)).thenReturn(listOf(action))

        deleteMissionNav.execute(id  = ownerId, serviceId = 4)
        verify(missionRepo, times(1)).finById(ownerId)
        verify(missionRepo, times(0)).deleteById(ownerId)
        verify(deleteNavAction, times(0)).execute(id  = action.id)

    }

    @Test
    fun `should not call delete if source is not rapportNav`() {
        val serviceId = 5
        val missionSource = MissionSourceEnum.MONITORENV
        val ownerId = UUID.randomUUID()
        val action = MissionNavActionEntityMock.create(id = UUID.randomUUID(), actionType = ActionType.INQUIRY)

        val  model = MissionModel(
            id = ownerId,
            serviceId = serviceId,
            startDateTimeUtc = Instant.now(),
            missionSource = missionSource
        )

        `when`(missionRepo.finById(id = ownerId)).thenReturn(Optional.of(model))
        `when`(getNavActionListByOwnerId.execute(ownerId = ownerId)).thenReturn(listOf(action))

        deleteMissionNav.execute(id  = ownerId, serviceId = 4)
        verify(missionRepo, times(1)).finById(ownerId)
        verify(missionRepo, times(0)).deleteById(ownerId)
        verify(deleteNavAction, times(0)).execute(id  = action.id)

    }


    @Test
    fun `should call delete of repository `() {
        val serviceId = 5
        val ownerId = UUID.randomUUID()
        val action = MissionNavActionEntityMock.create(id = UUID.randomUUID(), actionType = ActionType.INQUIRY)

        val  model = MissionModel(
            id = ownerId,
            serviceId = serviceId,
            startDateTimeUtc = Instant.now(),
            missionSource = MissionSourceEnum.RAPPORTNAV
        )

        `when`(missionRepo.finById(id = ownerId)).thenReturn(Optional.of(model))
        `when`(getNavActionListByOwnerId.execute(ownerId = ownerId)).thenReturn(listOf(action))

        deleteMissionNav.execute(id  = ownerId, serviceId = serviceId)
        verify(missionRepo, times(1)).deleteById(ownerId)
        verify(deleteNavAction, times(1)).execute(id  = action.id)

    }
}
