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

@SpringBootTest(classes = [GetStatusForAction::class])
class GetStatusForAction2Test {
    @Autowired
    private lateinit var useCase: GetStatusForAction

    @MockitoBean
    private lateinit var repository: IDBMissionActionRepository

    @Test
    fun `returns UNKNOWN when no actions`() {
        whenever(repository.findAllByMissionId(10)).thenReturn(emptyList())

        val result = useCase.execute(10)

        assertEquals(ActionStatusType.UNKNOWN, result)
    }


    @Test
    fun `returns UNKNOWN when no STATUS actions`() {
        val model = MissionActionModelMock.create()

        whenever(repository.findAllByMissionId(10)).thenReturn(listOf(model))

        val result = useCase.execute(10)

        assertEquals(ActionStatusType.UNKNOWN, result)
    }

    @Test
    fun `returns UNKNOWN if no STATUS before reference time`() {
        val t1 = MissionActionModelMock.create(actionType = ActionType.STATUS, status = ActionStatusType.ANCHORED, startDateTimeUtc =  Instant.parse("2020-01-02T00:00:00Z"))

        whenever(repository.findAllByMissionId(9)).thenReturn(listOf(t1))

        val ref = Instant.parse("2020-01-01T00:00:00Z")
        val result = useCase.execute(9, ref)

        assertEquals(ActionStatusType.UNKNOWN, result)
    }

    @Test
    fun `returns latest STATUS when no actionStartDateTimeUtc provided`() {
        val t1 = MissionActionModelMock.create(actionType = ActionType.STATUS, status = ActionStatusType.ANCHORED, startDateTimeUtc =  Instant.parse("2020-01-01T00:00:00Z"))
        val t2 = MissionActionModelMock.create(actionType = ActionType.STATUS, status = ActionStatusType.NAVIGATING, startDateTimeUtc = Instant.parse("2020-02-01T00:00:00Z"))

        whenever(repository.findAllByMissionId(1)).thenReturn(listOf(t1, t2))

        val result = useCase.execute(1, null)

        assertEquals(ActionStatusType.NAVIGATING, result)
    }

    @Test
    fun `returns last STATUS before reference time`() {
        val t1 = MissionActionModelMock.create(actionType = ActionType.STATUS, status = ActionStatusType.ANCHORED, startDateTimeUtc =  Instant.parse("2020-01-01T00:00:00Z"))
        val t2 = MissionActionModelMock.create(actionType = ActionType.STATUS, status = ActionStatusType.NAVIGATING, startDateTimeUtc =  Instant.parse("2020-01-02T00:00:00Z"))

        whenever(repository.findAllByMissionId(5)).thenReturn(
            listOf(t1, t2)
        )

        val ref = Instant.parse("2020-01-04T00:00:00Z")
        val result = useCase.execute(5, ref)

        assertEquals(ActionStatusType.NAVIGATING, result)
    }
}
