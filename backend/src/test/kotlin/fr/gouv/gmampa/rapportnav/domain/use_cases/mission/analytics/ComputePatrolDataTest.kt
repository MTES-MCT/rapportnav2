package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.analytics

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.ComputePatrolData
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.operationalSummary.ComputeAllOperationalSummary
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2.MapStatusDurations2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetComputeEnvMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionActionControls
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionEntityMock2
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionEnvActionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionNavActionEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant

@SpringBootTest(classes = [
    ComputePatrolData::class,
    MapStatusDurations2::class,
    ComputeAllOperationalSummary::class,
    GetMissionActionControls::class,
])
class ComputePatrolDataTest {

    @Autowired
    private lateinit var computePatrolData: ComputePatrolData

    @MockitoBean
    private lateinit var getComputeEnvMission: GetComputeEnvMission
    @MockitoBean
    private lateinit var computeAllOperationalSummary: ComputeAllOperationalSummary
    @MockitoBean
    private lateinit var mapStatusDurations2: MapStatusDurations2

    @Test
    fun `Should return null when no mission data found`() {
        `when`(getComputeEnvMission.execute(999)).thenReturn(null)
        val result = computePatrolData.execute(999)
        assertThat(result).isNull()
    }

    @Test
    fun `Should return the correct fields from envMission`() {
        val missionId = 123
        val missionMock = MissionEntityMock2.create(
            id = missionId,
            startDateTimeUtc = Instant.parse("2022-01-02T12:00:00Z"),
            endDateTimeUtc = Instant.parse("2022-01-02T13:00:00Z"),
        )
        `when`(getComputeEnvMission.execute(missionId)).thenReturn(missionMock)

        val actual = computePatrolData.execute(missionId = 123)
        assertThat(actual?.id).isEqualTo(123)
        assertThat(actual?.startDateTimeUtc).isEqualTo(Instant.parse("2022-01-02T12:00:00Z"))
        assertThat(actual?.endDateTimeUtc).isEqualTo(Instant.parse("2022-01-02T13:00:00Z"))
        assertThat(actual?.facade).isEqualTo(null)
        assertThat(actual?.missionTypes).isEqualTo(listOf<MissionTypeEnum>())
        assertThat(actual?.controlUnits).isEqualTo(listOf<LegacyControlUnitEntity>())
        assertThat(actual?.isDeleted).isEqualTo(false)
        assertThat(actual?.missionSource).isEqualTo(MissionSourceEnum.MONITORENV)
    }


    @Test
    fun `Should return the activity fields `() {
        val missionId = 123
        val actions = listOf<MissionActionEntity>(
            MissionNavActionEntityMock.create(
                actionType = ActionType.STATUS,
                status = ActionStatusType.DOCKED,
                reason = ActionStatusReason.MCO_AND_LOGISTICS,
                startDateTimeUtc = Instant.parse("2022-01-02T10:00:00Z"),
            ),
            MissionNavActionEntityMock.create(
                actionType = ActionType.STATUS,
                status = ActionStatusType.NAVIGATING,
                startDateTimeUtc = Instant.parse("2022-01-02T11:00:00Z"),
            ),
            MissionNavActionEntityMock.create(
                actionType = ActionType.CONTROL,
                startDateTimeUtc = Instant.parse("2022-01-02T11:00:00Z"),
                endDateTimeUtc = Instant.parse("2022-01-02T12:00:00Z")
            ),
            MissionNavActionEntityMock.create(
                actionType = ActionType.VIGIMER,
                startDateTimeUtc = Instant.parse("2022-01-02T12:00:00Z"),
                endDateTimeUtc = Instant.parse("2022-01-02T13:00:00Z")
            ),
            MissionNavActionEntityMock.create(
                actionType = ActionType.BAAEM_PERMANENCE,
                startDateTimeUtc = Instant.parse("2022-01-02T14:00:00Z"),
                endDateTimeUtc = Instant.parse("2022-01-02T15:00:00Z")
            ),
            MissionNavActionEntityMock.create(
                actionType = ActionType.RESCUE,
                startDateTimeUtc = Instant.parse("2022-01-02T16:00:00Z"),
                endDateTimeUtc = Instant.parse("2022-01-02T17:00:00Z")
            ),
            MissionNavActionEntityMock.create(
                actionType = ActionType.STATUS,
                status = ActionStatusType.ANCHORED,
                startDateTimeUtc = Instant.parse("2022-01-02T11:00:00Z"),
            ),
            MissionEnvActionEntityMock.create(
                envActionType = ActionTypeEnum.SURVEILLANCE,
                startDateTimeUtc = Instant.parse("2022-01-02T17:00:00Z"),
                endDateTimeUtc = Instant.parse("2022-01-02T18:00:00Z")
            ),
            MissionEnvActionEntityMock.create(
                envActionType = ActionTypeEnum.CONTROL,
                startDateTimeUtc = Instant.parse("2022-01-02T18:00:00Z"),
                endDateTimeUtc = Instant.parse("2022-01-02T19:00:00Z")
            ),
            MissionNavActionEntityMock.create(
                actionType = ActionType.STATUS,
                status = ActionStatusType.UNKNOWN,
                reason = ActionStatusReason.TECHNICAL,
                startDateTimeUtc = Instant.parse("2022-01-02T19:00:00Z"),
            ),
        )
        val missionMock = MissionEntityMock2.create(
            id = missionId,
            startDateTimeUtc = Instant.parse("2022-01-02T12:00:00Z"),
            endDateTimeUtc = Instant.parse("2022-01-03T13:00:00Z"),
            actions = actions
        )

        val expected = mapOf(
            "atSea" to mapOf(
                "anchoredDurationInHours" to 2.0,
                "navigationDurationInHours" to 2.0,
                "totalDurationInHours" to 4.0,
                "amountOfControls" to 0.0,
            ),
            "docked" to mapOf(
                "adminFormationDurationInHours" to 0.0,
                "otherDurationInHours" to 0.0,
                "contrPolDurationInHours" to 0.0,
                "maintenanceDurationInHours" to 0.0,
                "meteoDurationInHours" to 0.0,
                "representationDurationInHours" to 0.0,
                "mcoDurationInHours" to 1.0,
                "totalDurationInHours" to 1.0,
                "amountOfControls" to 0.0,
            ),
            "unavailable" to mapOf(
                "personnelDurationInHours" to 2.0,
                "technicalDurationInHours" to 0.0,
                "totalDurationInHours" to 2.0,
                "amountOfControls" to 0.0,
            )
        )

        `when`(getComputeEnvMission.execute(missionId)).thenReturn(missionMock)
        `when`(mapStatusDurations2.execute(anyList<MissionNavActionEntity>(), anyList<MissionActionEntity>(), any())).thenReturn(expected)

        val actual = computePatrolData.execute(missionId = 123)
        assertThat(actual?.activity).isEqualTo(expected)
    }


}
