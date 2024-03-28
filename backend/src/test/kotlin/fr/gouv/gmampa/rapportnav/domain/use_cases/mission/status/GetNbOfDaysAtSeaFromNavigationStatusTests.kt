package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.status

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status.GetNbOfDaysAtSeaFromNavigationStatus
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status.GetStatusDurations
import fr.gouv.gmampa.rapportnav.mocks.mission.action.NavActionStatusMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import kotlin.time.DurationUnit


@SpringBootTest(classes = [GetNbOfDaysAtSeaFromNavigationStatus::class])

class GetNbOfDaysAtSeaFromNavigationStatusTests {

    @Autowired
    private lateinit var getNbOfDaysAtSeaFromNavigationStatus: GetNbOfDaysAtSeaFromNavigationStatus

    @MockBean
    private lateinit var getStatusDurations: GetStatusDurations

    private val missionStartDateTime = ZonedDateTime.of(LocalDateTime.of(2022, 1, 1, 12, 0), ZoneOffset.UTC)
    private val missionEndDateTime = ZonedDateTime.of(LocalDateTime.of(2022, 1, 2, 22, 0), ZoneOffset.UTC)


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
    fun `execute should return 0 when no Navigation statuses`() {
        val statuses = listOf(NavActionStatusMock.createActionStatusEntity(status = ActionStatusType.ANCHORED))
        val value = getNbOfDaysAtSeaFromNavigationStatus.execute(
            missionStartDateTime,
            missionEndDateTime,
            statuses
        );
        assertThat(value).isEqualTo(0)
    }

    @Test
    fun `execute should return the amount of days exceeding fours hours of navigation`() {
        // first day has only one hour of navigation
        // second day has 6
        val startDateTime = ZonedDateTime.of(LocalDateTime.of(2022, 1, 1, 12, 0), ZoneOffset.UTC)
        val statuses = listOf(
            NavActionStatusMock.createActionStatusEntity(
                status = ActionStatusType.ANCHORED,
                startDateTimeUtc = startDateTime
            ),
            NavActionStatusMock.createActionStatusEntity(
                status = ActionStatusType.NAVIGATING,
                startDateTimeUtc = startDateTime.plusHours(1)
            ),
            NavActionStatusMock.createActionStatusEntity(
                status = ActionStatusType.ANCHORED,
                startDateTimeUtc = startDateTime.plusHours(2)
            ),
            NavActionStatusMock.createActionStatusEntity(
                status = ActionStatusType.NAVIGATING,
                startDateTimeUtc = startDateTime.plusHours(24)
            ),
            NavActionStatusMock.createActionStatusEntity(
                status = ActionStatusType.ANCHORED,
                startDateTimeUtc = startDateTime.plusHours(30)
            ),
        )
        val mock = mutableListOf(
            GetStatusDurations.ActionStatusWithDuration(
                status = ActionStatusType.ANCHORED,
                duration = 1.0,
                date = startDateTime.toLocalDate()
            ),
            GetStatusDurations.ActionStatusWithDuration(
                status = ActionStatusType.NAVIGATING,
                duration = 1.0,
                date = startDateTime.plusHours(1).toLocalDate()
            ),
            GetStatusDurations.ActionStatusWithDuration(
                status = ActionStatusType.ANCHORED,
                duration = 22.0,
                date = startDateTime.plusHours(2).toLocalDate()
            ),
            GetStatusDurations.ActionStatusWithDuration(
                status = ActionStatusType.NAVIGATING,
                duration = 6.0,
                date = startDateTime.plusHours(24).toLocalDate()
            ),
            GetStatusDurations.ActionStatusWithDuration(
                status = ActionStatusType.ANCHORED,
                duration = 6.0,
                date = startDateTime.plusHours(30).toLocalDate()
            ),
        )
        given(
            this.getStatusDurations.computeDurationsByAction(
                missionStartDateTime = missionStartDateTime,
                missionEndDateTime = missionEndDateTime,
                actions = statuses,
                durationUnit = DurationUnit.HOURS
            )
        ).willReturn(mock)
        val value = getNbOfDaysAtSeaFromNavigationStatus.execute(
            missionStartDateTime,
            missionEndDateTime,
            statuses
        );
        assertThat(value).isEqualTo(1)
    }


}
