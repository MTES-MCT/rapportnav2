package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.MapStatusDurations
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status.GetStatusDurations
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.NavActionStatusMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime

@SpringBootTest(classes = [MapStatusDurations::class])
class MapStatusDurationsTests {

    @Autowired
    private lateinit var mapStatusDurations: MapStatusDurations

    @MockBean
    private lateinit var getStatusDurations: GetStatusDurations

    @Test
    fun `formatTimeline should return null when null given`() {
        val inputMission = MissionEntityMock.create(
            startDateTimeUtc = ZonedDateTime.of(LocalDateTime.of(2022, 1, 2, 12, 0), ZoneOffset.UTC),
            endDateTimeUtc = ZonedDateTime.of(LocalDateTime.of(2022, 1, 2, 22, 0), ZoneOffset.UTC),
        )
        val inputStatuses = listOf(
            NavActionStatusMock.createActionStatusEntity(
                status = ActionStatusType.ANCHORED,
                startDateTimeUtc = ZonedDateTime.of(LocalDateTime.of(2022, 1, 2, 12, 0), ZoneOffset.UTC)
            ),
            NavActionStatusMock.createActionStatusEntity(
                status = ActionStatusType.DOCKED,
                reason = ActionStatusReason.WEATHER,
                startDateTimeUtc = ZonedDateTime.of(LocalDateTime.of(2022, 1, 2, 14, 0), ZoneOffset.UTC)
            ),
            NavActionStatusMock.createActionStatusEntity(
                status = ActionStatusType.DOCKED,
                reason = ActionStatusReason.REPRESENTATION,
                startDateTimeUtc = ZonedDateTime.of(LocalDateTime.of(2022, 1, 2, 16, 0), ZoneOffset.UTC)
            ),
            NavActionStatusMock.createActionStatusEntity(
                status = ActionStatusType.NAVIGATING,
                startDateTimeUtc = ZonedDateTime.of(LocalDateTime.of(2022, 1, 2, 18, 0), ZoneOffset.UTC)
            ),
            NavActionStatusMock.createActionStatusEntity(
                status = ActionStatusType.UNAVAILABLE,
                reason = ActionStatusReason.PERSONNEL,
                startDateTimeUtc = ZonedDateTime.of(LocalDateTime.of(2022, 1, 2, 20, 0), ZoneOffset.UTC)
            )
        )
        val mockData = listOf(
            GetStatusDurations.ActionStatusWithDuration(
                status = ActionStatusType.ANCHORED,
                duration = 120.0,
                reason = null
            ),
            GetStatusDurations.ActionStatusWithDuration(
                status = ActionStatusType.DOCKED,
                duration = 0.0,
                reason = ActionStatusReason.MAINTENANCE
            ),
            GetStatusDurations.ActionStatusWithDuration(
                status = ActionStatusType.DOCKED,
                duration = 120.0,
                reason = ActionStatusReason.WEATHER
            ),
            GetStatusDurations.ActionStatusWithDuration(
                status = ActionStatusType.DOCKED,
                duration = 120.0,
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
                duration = 120.0,
                reason = null
            ),
            GetStatusDurations.ActionStatusWithDuration(
                status = ActionStatusType.UNAVAILABLE,
                duration = 0.0,
                reason = ActionStatusReason.TECHNICAL
            ),
            GetStatusDurations.ActionStatusWithDuration(
                status = ActionStatusType.UNAVAILABLE,
                duration = 120.0,
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
                missionStartDateTime = inputMission.startDateTimeUtc,
                missionEndDateTime = inputMission.endDateTimeUtc,
                actions = inputStatuses
            )
        ).thenReturn(mockData)
        assertThat(mapStatusDurations.execute(inputMission, inputStatuses)).isEqualTo(
            mapOf(
                "atSeaDurations" to mapOf(
                    "mouillage" to 120, "navigationEffective" to 120, "total" to 240
                ),
                "dockingDurations" to mapOf(
                    "adminFormation" to 0,
                    "autre" to 0,
                    "contrPol" to 0,
                    "maintenance" to 0,
                    "meteo" to 120,
                    "representation" to 120,
                    "total" to 240
                ),
                "unavailabilityDurations" to mapOf(
                    "personnel" to 120, "technique" to 0, "total" to 120
                )
            )
        )

    }


}
