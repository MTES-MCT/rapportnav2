package fr.gouv.gmampa.rapportnav.domain.use_cases.analytics

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
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
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.patrol.ComputeInternTrainingSummary
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.patrol.controlPolicies.ComputeControlPolicies
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.patrol.operationalSummary.ComputeAllOperationalSummary
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.GetInfoAboutNavAction2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.MapStatusDurations
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetComputeEnvMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionActionControls
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionEntityMock
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
    MapStatusDurations::class,
    ComputeAllOperationalSummary::class,
    ComputeControlPolicies::class,
    GetMissionActionControls::class,
    ComputeInternTrainingSummary::class,
    GetInfoAboutNavAction2::class,
])
class ComputePatrolDataTest {

    @Autowired
    private lateinit var computePatrolData: ComputePatrolData

    @MockitoBean
    private lateinit var getComputeEnvMission: GetComputeEnvMission
    @MockitoBean
    private lateinit var computeAllOperationalSummary: ComputeAllOperationalSummary
    @MockitoBean
    private lateinit var computeControlPolicies: ComputeControlPolicies
    @MockitoBean
    private lateinit var computeInternTrainingSummary: ComputeInternTrainingSummary
    @MockitoBean
    private lateinit var getInfoAboutNavAction2: GetInfoAboutNavAction2
    @MockitoBean
    private lateinit var mapStatusDurations: MapStatusDurations

    @Test
    fun `Should return correct mission id`() {
        val missionId = 999
        val missionMock = MissionEntityMock.create(id = missionId)
        `when`(getComputeEnvMission.execute(missionId)).thenReturn(missionMock)

        val result = computePatrolData.execute(missionId)
        assertThat(result?.id).isEqualTo(missionId)
    }

    @Test
    fun `Should return the correct fields from envMission`() {
        val missionId = 123
        val missionMock = MissionEntityMock.create(
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
        assertThat(actual?.completenessForStats).isNotNull()
        assertThat(actual?.isMissionFinished).isTrue() // endDateTimeUtc is in the past
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
        val missionMock = MissionEntityMock.create(
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
                "nbDaysAtSea" to 0.0,
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

        `when`(getComputeEnvMission.execute(missionId))
            .thenReturn(missionMock)
        `when`(mapStatusDurations.execute(anyList<MissionNavActionEntity>(), anyList<MissionActionEntity>(), any(), any()))
            .thenReturn(expected)

        val actual = computePatrolData.execute(missionId = 123)
        assertThat(actual?.activity).isEqualTo(expected)
    }

    @Test
    fun `Should return isMissionFinished false when endDateTimeUtc is in the future`() {
        val missionId = 123
        val futureDate = Instant.now().plusSeconds(86400) // tomorrow
        val missionMock = MissionEntityMock.create(
            id = missionId,
            startDateTimeUtc = Instant.parse("2022-01-02T12:00:00Z"),
            endDateTimeUtc = futureDate,
        )
        `when`(getComputeEnvMission.execute(missionId)).thenReturn(missionMock)

        val actual = computePatrolData.execute(missionId = missionId)

        assertThat(actual?.isMissionFinished).isFalse()
    }

    @Test
    fun `Should return isMissionFinished true when endDateTimeUtc is in the past`() {
        val missionId = 123
        val pastDate = Instant.parse("2022-01-02T13:00:00Z")
        val missionMock = MissionEntityMock.create(
            id = missionId,
            startDateTimeUtc = Instant.parse("2022-01-02T12:00:00Z"),
            endDateTimeUtc = pastDate,
        )
        `when`(getComputeEnvMission.execute(missionId)).thenReturn(missionMock)

        val actual = computePatrolData.execute(missionId = missionId)

        assertThat(actual?.isMissionFinished).isTrue()
    }

    @Test
    fun `Should return completenessForStats from mission`() {
        val missionId = 123
        val missionMock = MissionEntityMock.create(id = missionId)
        `when`(getComputeEnvMission.execute(missionId)).thenReturn(missionMock)

        val actual = computePatrolData.execute(missionId = missionId)

        assertThat(actual?.completenessForStats).isNotNull()
        assertThat(actual?.completenessForStats?.status).isIn(
            CompletenessForStatsStatusEnum.COMPLETE,
            CompletenessForStatsStatusEnum.INCOMPLETE
        )
    }
}
