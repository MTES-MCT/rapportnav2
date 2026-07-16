package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBMissionActionRepository
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionActionModelMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [GetStatusForAction::class])
class GetStatusForAction2Test {
    @Autowired
    private lateinit var useCase: GetStatusForAction

    @MockitoBean
    private lateinit var repository: IDBMissionActionRepository

    private val missionId1 = UUID.randomUUID()
    private val missionId2 = UUID.randomUUID()
    private val missionId3 = UUID.randomUUID()
    private val missionId4 = UUID.randomUUID()

    @Test
    fun `returns UNKNOWN when no actions`() {
        whenever(repository.findAllByOwnerId(missionId1)).thenReturn(emptyList())

        val result = useCase.execute(missionId1)

        assertEquals(ActionStatusType.UNKNOWN, result)
    }


    @Test
    fun `returns UNKNOWN when no STATUS actions`() {
        val model = MissionActionModelMock.create()

        whenever(repository.findAllByOwnerId(missionId1)).thenReturn(listOf(model))

        val result = useCase.execute(missionId1)

        assertEquals(ActionStatusType.UNKNOWN, result)
    }

    @Test
    fun `returns UNKNOWN if no STATUS before reference time`() {
        val t1 = MissionActionModelMock.create(actionType = ActionType.STATUS, status = ActionStatusType.ANCHORED, startDateTimeUtc =  Instant.parse("2020-01-02T00:00:00Z"))

        whenever(repository.findAllByOwnerId(missionId2)).thenReturn(listOf(t1))

        val ref = Instant.parse("2020-01-01T00:00:00Z")
        val result = useCase.execute(missionId2, ref)

        assertEquals(ActionStatusType.UNKNOWN, result)
    }

    @Test
    fun `returns latest STATUS when no actionStartDateTimeUtc provided`() {
        val t1 = MissionActionModelMock.create(actionType = ActionType.STATUS, status = ActionStatusType.ANCHORED, startDateTimeUtc =  Instant.parse("2020-01-01T00:00:00Z"))
        val t2 = MissionActionModelMock.create(actionType = ActionType.STATUS, status = ActionStatusType.NAVIGATING, startDateTimeUtc = Instant.parse("2020-02-01T00:00:00Z"))

        whenever(repository.findAllByOwnerId(missionId3)).thenReturn(listOf(t1, t2))

        val result = useCase.execute(missionId3, null)

        assertEquals(ActionStatusType.NAVIGATING, result)
    }

    @Test
    fun `returns last STATUS before reference time`() {
        val t1 = MissionActionModelMock.create(actionType = ActionType.STATUS, status = ActionStatusType.ANCHORED, startDateTimeUtc =  Instant.parse("2020-01-01T00:00:00Z"))
        val t2 = MissionActionModelMock.create(actionType = ActionType.STATUS, status = ActionStatusType.NAVIGATING, startDateTimeUtc =  Instant.parse("2020-01-02T00:00:00Z"))

        whenever(repository.findAllByOwnerId(missionId4)).thenReturn(
            listOf(t1, t2)
        )

        val ref = Instant.parse("2020-01-04T00:00:00Z")
        val result = useCase.execute(missionId4, ref)

        assertEquals(ActionStatusType.NAVIGATING, result)
    }
}
