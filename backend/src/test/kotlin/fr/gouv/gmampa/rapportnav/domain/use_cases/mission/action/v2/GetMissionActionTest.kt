package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeEnvActionListByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeFishActionListByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeNavActionListByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetMissionAction
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionNavActionEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant

@SpringBootTest(classes = [GetMissionAction::class])
@ContextConfiguration(classes = [GetMissionAction::class])
class GetMissionActionTest {

    @Autowired
    private lateinit var getMissionAction: GetMissionAction

    @MockitoBean
    private lateinit var getEnvActionByMissionId: GetComputeEnvActionListByMissionId

    @MockitoBean
    private lateinit var getNavActionByMissionId: GetComputeNavActionListByMissionId

    @MockitoBean
    private lateinit var getFishListActionByMissionId: GetComputeFishActionListByMissionId

    private val missionId = 761

    @Test
    fun `should return actions sorted by startDateTimeUtc descending`() {
        val early = MissionNavActionEntityMock.create(
            missionId = missionId,
            actionType = ActionType.CONTROL,
            startDateTimeUtc = Instant.parse("2024-01-01T08:00:00Z")
        )
        val late = MissionNavActionEntityMock.create(
            missionId = missionId,
            actionType = ActionType.CONTROL,
            startDateTimeUtc = Instant.parse("2024-01-01T16:00:00Z")
        )

        `when`(getEnvActionByMissionId.execute(missionId)).thenReturn(emptyList())
        `when`(getNavActionByMissionId.execute(missionId)).thenReturn(listOf(early, late))
        `when`(getFishListActionByMissionId.execute(missionId)).thenReturn(emptyList())

        val result = getMissionAction.execute(missionId)

        assertThat(result).hasSize(2)
        assertThat(result[0].startDateTimeUtc).isEqualTo(Instant.parse("2024-01-01T16:00:00Z"))
        assertThat(result[1].startDateTimeUtc).isEqualTo(Instant.parse("2024-01-01T08:00:00Z"))
    }

    @Test
    fun `should compute status from STATUS nav actions`() {
        val statusAtSea = MissionNavActionEntityMock.create(
            missionId = missionId,
            actionType = ActionType.STATUS,
            status = ActionStatusType.NAVIGATING,
            startDateTimeUtc = Instant.parse("2024-01-01T06:00:00Z")
        )
        val statusDocked = MissionNavActionEntityMock.create(
            missionId = missionId,
            actionType = ActionType.STATUS,
            status = ActionStatusType.DOCKED,
            startDateTimeUtc = Instant.parse("2024-01-01T14:00:00Z")
        )
        val controlAction = MissionNavActionEntityMock.create(
            missionId = missionId,
            actionType = ActionType.CONTROL,
            startDateTimeUtc = Instant.parse("2024-01-01T10:00:00Z")
        )

        `when`(getEnvActionByMissionId.execute(missionId)).thenReturn(emptyList())
        `when`(getNavActionByMissionId.execute(missionId)).thenReturn(listOf(statusAtSea, statusDocked, controlAction))
        `when`(getFishListActionByMissionId.execute(missionId)).thenReturn(emptyList())

        val result = getMissionAction.execute(missionId)
        val control = result.first { it.startDateTimeUtc == Instant.parse("2024-01-01T10:00:00Z") }

        // control at 10:00 should get NAVIGATING (last STATUS before 10:00 is the 06:00 one)
        assertThat(control.status).isEqualTo(ActionStatusType.NAVIGATING)
    }

    @Test
    fun `should return UNKNOWN status when no STATUS actions exist`() {
        val controlAction = MissionNavActionEntityMock.create(
            missionId = missionId,
            actionType = ActionType.CONTROL,
            startDateTimeUtc = Instant.parse("2024-01-01T10:00:00Z")
        )

        `when`(getEnvActionByMissionId.execute(missionId)).thenReturn(emptyList())
        `when`(getNavActionByMissionId.execute(missionId)).thenReturn(listOf(controlAction))
        `when`(getFishListActionByMissionId.execute(missionId)).thenReturn(emptyList())

        val result = getMissionAction.execute(missionId)

        assertThat(result).hasSize(1)
        assertThat(result[0].status).isEqualTo(ActionStatusType.UNKNOWN)
    }

    @Test
    fun `should return UNKNOWN when action is before any STATUS`() {
        val statusAction = MissionNavActionEntityMock.create(
            missionId = missionId,
            actionType = ActionType.STATUS,
            status = ActionStatusType.NAVIGATING,
            startDateTimeUtc = Instant.parse("2024-01-01T12:00:00Z")
        )
        val earlyControl = MissionNavActionEntityMock.create(
            missionId = missionId,
            actionType = ActionType.CONTROL,
            startDateTimeUtc = Instant.parse("2024-01-01T08:00:00Z")
        )

        `when`(getEnvActionByMissionId.execute(missionId)).thenReturn(emptyList())
        `when`(getNavActionByMissionId.execute(missionId)).thenReturn(listOf(statusAction, earlyControl))
        `when`(getFishListActionByMissionId.execute(missionId)).thenReturn(emptyList())

        val result = getMissionAction.execute(missionId)
        val control = result.first { it.startDateTimeUtc == Instant.parse("2024-01-01T08:00:00Z") }

        assertThat(control.status).isEqualTo(ActionStatusType.UNKNOWN)
    }
}
