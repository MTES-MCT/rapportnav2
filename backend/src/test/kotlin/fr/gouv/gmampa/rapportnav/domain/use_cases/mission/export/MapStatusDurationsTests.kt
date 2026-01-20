package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.MapStatusDurations
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status.GetNbOfDaysAtSeaFromNavigationStatus
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status.GetStatusDurations
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionEntityMock2
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionNavActionEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import kotlin.time.DurationUnit

@SpringBootTest(classes = [MapStatusDurations::class, GetNbOfDaysAtSeaFromNavigationStatus::class])
class MapStatusDurationsTests {

    @Autowired
    private lateinit var mapStatusDurations: MapStatusDurations

    @MockitoBean
    private lateinit var getStatusDurations: GetStatusDurations

    @MockitoBean
    private lateinit var getNbOfDaysAtSeaFromNavigationStatus: GetNbOfDaysAtSeaFromNavigationStatus

     @Test
    fun `MapStatusDurations should return data`() {
        val inputMission = MissionEntityMock2.create(
            startDateTimeUtc = Instant.parse("2022-01-02T12:00:00.000+01:00"),
            endDateTimeUtc = Instant.parse("2022-01-02T22:00:00.000+01:00")
        )

        val inputStatuses = listOf(
            MissionNavActionEntityMock.create(
                actionType = ActionType.STATUS,
                status = ActionStatusType.ANCHORED,
                startDateTimeUtc = Instant.parse("2022-01-02T12:00:00.000+01:00"),
            ),
            MissionNavActionEntityMock.create(
                actionType = ActionType.STATUS,
                status = ActionStatusType.DOCKED,
                reason = ActionStatusReason.WEATHER,
                startDateTimeUtc = Instant.parse("2022-01-02T14:00:00.000+01:00"),
            ),
            MissionNavActionEntityMock.create(
                actionType = ActionType.STATUS,
                status = ActionStatusType.DOCKED,
                reason = ActionStatusReason.MCO_AND_LOGISTICS,
                startDateTimeUtc = Instant.parse("2022-01-02T15:00:00.000+01:00"),
            ),
            MissionNavActionEntityMock.create(
                actionType = ActionType.STATUS,
                status = ActionStatusType.DOCKED,
                reason = ActionStatusReason.REPRESENTATION,
                startDateTimeUtc = Instant.parse("2022-01-02T16:00:00.000+01:00"),
            ),
            MissionNavActionEntityMock.create(
                actionType = ActionType.STATUS,
                status = ActionStatusType.NAVIGATING,
                startDateTimeUtc = Instant.parse("2022-01-02T18:00:00.000+01:00"),
            ),
            MissionNavActionEntityMock.create(
                actionType = ActionType.STATUS,
                status = ActionStatusType.UNAVAILABLE,
                reason = ActionStatusReason.PERSONNEL,
                startDateTimeUtc = Instant.parse("2022-01-02T20:00:00.000+01:00"),
            )
        )
        val mockData = listOf(
            GetStatusDurations.ActionStatusWithDuration(
                status = ActionStatusType.ANCHORED,
                duration = 2.0,
                reason = null
            ),
            GetStatusDurations.ActionStatusWithDuration(
                status = ActionStatusType.DOCKED,
                duration = 0.0,
                reason = ActionStatusReason.MAINTENANCE
            ),
            GetStatusDurations.ActionStatusWithDuration(
                status = ActionStatusType.DOCKED,
                duration = 1.0,
                reason = ActionStatusReason.WEATHER
            ),
            GetStatusDurations.ActionStatusWithDuration(
                status = ActionStatusType.DOCKED,
                duration = 1.0,
                reason = ActionStatusReason.MCO_AND_LOGISTICS
            ),
            GetStatusDurations.ActionStatusWithDuration(
                status = ActionStatusType.DOCKED,
                duration = 2.0,
                reason = ActionStatusReason.REPRESENTATION
            ),
            GetStatusDurations.ActionStatusWithDuration(
                status = ActionStatusType.DOCKED,
                duration = 0.0,
                reason = ActionStatusReason.ADMINISTRATION
            ),
            GetStatusDurations.ActionStatusWithDuration(
                status = ActionStatusType.DOCKED,
                duration = 0.0,
                reason = ActionStatusReason.HARBOUR_CONTROL
            ),
            GetStatusDurations.ActionStatusWithDuration(
                status = ActionStatusType.DOCKED,
                duration = 0.0,
                reason = ActionStatusReason.OTHER
            ),
            GetStatusDurations.ActionStatusWithDuration(
                status = ActionStatusType.NAVIGATING,
                duration = 2.0,
                reason = null
            ),
            GetStatusDurations.ActionStatusWithDuration(
                status = ActionStatusType.UNAVAILABLE,
                duration = 0.0,
                reason = ActionStatusReason.TECHNICAL
            ),
            GetStatusDurations.ActionStatusWithDuration(
                status = ActionStatusType.UNAVAILABLE,
                duration = 2.0,
                reason = ActionStatusReason.PERSONNEL
            ),
            GetStatusDurations.ActionStatusWithDuration(
                status = ActionStatusType.UNAVAILABLE,
                duration = 0.0,
                reason = ActionStatusReason.OTHER
            )
        )

        Mockito.`when`(
            getStatusDurations.computeActionDurationsForAllMission(
                missionEndDateTime = inputMission.data?.endDateTimeUtc,
                actions = inputStatuses,
                durationUnit = DurationUnit.HOURS
            )
        ).thenReturn(mockData)

        Mockito.`when`(
            getNbOfDaysAtSeaFromNavigationStatus.execute(
                missionStartDateTime = inputMission.data?.startDateTimeUtc,
                missionEndDateTime = inputMission.data?.endDateTimeUtc,
                actions = inputStatuses,
                durationUnit = DurationUnit.HOURS
            )
        ).thenReturn(3)

        assertThat(
            mapStatusDurations.execute(
                statuses =  inputStatuses,
                startDateTimeUtc = inputMission.data?.startDateTimeUtc,
                endDateTimeUtc = inputMission.data?.endDateTimeUtc,
            )
        ).isEqualTo(
            mapOf(
                "atSea" to mapOf(
                    "anchoredDurationInHours" to 2.0,
                    "navigationDurationInHours" to 2.0,
                    "totalDurationInHours" to 4.0,
                    "nbOfDaysAtSea" to 3.0,
                    "nbControls" to 0.0,
                ),
                "docked" to mapOf(
                    "adminFormationDurationInHours" to 0.0,
                    "otherDurationInHours" to 0.0,
                    "contrPolDurationInHours" to 0.0,
                    "maintenanceDurationInHours" to 0.0,
                    "meteoDurationInHours" to 1.0,
                    "representationDurationInHours" to 2.0,
                    "mcoDurationInHours" to 1.0,
                    "totalDurationInHours" to 4.0,
                    "nbControls" to 0.0,
                ),
                "unavailable" to mapOf(
                    "personnelDurationInHours" to 2.0,
                    "technicalDurationInHours" to 0.0,
                    "totalDurationInHours" to 2.0,
                    "nbControls" to 0.0,
                )
            )
        )

    }


}
