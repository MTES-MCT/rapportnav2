package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.status

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status.GetNbOfDaysAtSeaFromNavigationStatus
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status.GetStatusDurations
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.ComputeDurations
import fr.gouv.gmampa.rapportnav.mocks.mission.action.NavActionStatusMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime


@SpringBootTest(classes = [GetNbOfDaysAtSeaFromNavigationStatus::class, GetStatusDurations::class, ComputeDurations::class])

class GetNbOfDaysAtSeaFromNavigationStatusTests {

    @Autowired
    private lateinit var getNbOfDaysAtSeaFromNavigationStatus: GetNbOfDaysAtSeaFromNavigationStatus

    private val missionStartDateTime = ZonedDateTime.of(LocalDateTime.of(2022, 1, 1, 12, 0), ZoneOffset.UTC)
    private val missionEndDateTime = ZonedDateTime.of(LocalDateTime.of(2022, 1, 12, 22, 0), ZoneOffset.UTC)


    @Test
    fun `execute should return 0 when no statuses at all`() {
        val statuses = null
        val value = getNbOfDaysAtSeaFromNavigationStatus.execute(
            missionStartDateTime,
            missionEndDateTime,
            statuses
        );
        assertThat(value).isEqualTo(0)
    }

    @Test
    fun `execute should return 0 when no Navigation or Anchored statuses`() {
        val statuses = listOf(NavActionStatusMock.createActionStatusEntity(status = ActionStatusType.DOCKED))
        val value = getNbOfDaysAtSeaFromNavigationStatus.execute(
            missionStartDateTime,
            missionEndDateTime,
            statuses
        );
        assertThat(value).isEqualTo(0)
    }

    @Test
    fun `execute should return the amount of days exceeding fours hours of navigation`() {
        val statuses = listOf(
            // first day has only 2 hours of navigation
            NavActionStatusMock.createActionStatusEntity(
                status = ActionStatusType.NAVIGATING,
                startDateTimeUtc = missionStartDateTime
            ),
            NavActionStatusMock.createActionStatusEntity(
                status = ActionStatusType.DOCKED,
                startDateTimeUtc = missionStartDateTime.plusHours(1)
            ),
            NavActionStatusMock.createActionStatusEntity(
                status = ActionStatusType.ANCHORED,
                startDateTimeUtc = missionStartDateTime.plusHours(2)
            ),
            NavActionStatusMock.createActionStatusEntity(
                status = ActionStatusType.UNAVAILABLE,
                startDateTimeUtc = missionStartDateTime.plusHours(3)
            ),
            // second day has 7
            NavActionStatusMock.createActionStatusEntity(
                status = ActionStatusType.NAVIGATING,
                startDateTimeUtc = missionStartDateTime.plusHours(25)
            ),
            NavActionStatusMock.createActionStatusEntity(
                status = ActionStatusType.ANCHORED,
                startDateTimeUtc = missionStartDateTime.plusHours(30)
            ),
            NavActionStatusMock.createActionStatusEntity(
                status = ActionStatusType.UNAVAILABLE,
                startDateTimeUtc = missionStartDateTime.plusHours(32)
            ),
            // the rest of the days doesn't count
        )

        val value = getNbOfDaysAtSeaFromNavigationStatus.execute(
            missionStartDateTime,
            missionEndDateTime,
            statuses
        );
        assertThat(value).isEqualTo(1)
    }

    @Test
    fun `execute should return the amount of days exceeding fours hours of navigation even when a status spans over several days`() {
        val startDateTime = ZonedDateTime.of(LocalDateTime.of(2024, 3, 11, 7, 0), ZoneOffset.UTC)
        val endDateTime = ZonedDateTime.of(LocalDateTime.of(2024, 3, 22, 18, 0), ZoneOffset.UTC)
        val statuses = listOf(
            // day 1
            NavActionStatusMock.createActionStatusEntity(
                status = ActionStatusType.DOCKED,
                startDateTimeUtc = ZonedDateTime.of(LocalDateTime.of(2024, 3, 11, 4, 0), ZoneOffset.UTC)
            ),
            // day 2
            NavActionStatusMock.createActionStatusEntity(
                status = ActionStatusType.NAVIGATING,
                startDateTimeUtc = ZonedDateTime.of(LocalDateTime.of(2024, 3, 12, 6, 48), ZoneOffset.UTC)
            ),
            NavActionStatusMock.createActionStatusEntity(
                status = ActionStatusType.DOCKED,
                startDateTimeUtc = ZonedDateTime.of(LocalDateTime.of(2024, 3, 12, 16, 54), ZoneOffset.UTC)
            ),
            // day 3
            NavActionStatusMock.createActionStatusEntity(
                status = ActionStatusType.NAVIGATING,
                startDateTimeUtc = ZonedDateTime.of(LocalDateTime.of(2024, 3, 13, 7, 12), ZoneOffset.UTC)
            ),
            // day 4
            NavActionStatusMock.createActionStatusEntity(
                status = ActionStatusType.DOCKED,
                startDateTimeUtc = ZonedDateTime.of(LocalDateTime.of(2024, 3, 14, 21, 48), ZoneOffset.UTC)
            ),
            // day 5
            NavActionStatusMock.createActionStatusEntity(
                status = ActionStatusType.NAVIGATING,
                startDateTimeUtc = ZonedDateTime.of(LocalDateTime.of(2024, 3, 15, 19, 54), ZoneOffset.UTC)
            ),
            // skip to day 10
            NavActionStatusMock.createActionStatusEntity(
                status = ActionStatusType.ANCHORED,
                startDateTimeUtc = ZonedDateTime.of(LocalDateTime.of(2024, 3, 20, 18, 0), ZoneOffset.UTC)
            ),
            // day 11
            NavActionStatusMock.createActionStatusEntity(
                status = ActionStatusType.NAVIGATING,
                startDateTimeUtc = ZonedDateTime.of(LocalDateTime.of(2024, 3, 21, 7, 54), ZoneOffset.UTC)
            ),
            NavActionStatusMock.createActionStatusEntity(
                status = ActionStatusType.DOCKED,
                startDateTimeUtc = ZonedDateTime.of(LocalDateTime.of(2024, 3, 21, 13, 42), ZoneOffset.UTC)
            ),
            // day 12
            NavActionStatusMock.createActionStatusEntity(
                status = ActionStatusType.DOCKED,
                startDateTimeUtc = ZonedDateTime.of(LocalDateTime.of(2024, 3, 22, 16, 0), ZoneOffset.UTC)
            ),
        )

        val value = getNbOfDaysAtSeaFromNavigationStatus.execute(
            startDateTime,
            endDateTime,
            statuses
        );
        assertThat(value).isEqualTo(10)
    }


}
