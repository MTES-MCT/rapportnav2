package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.status

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatusEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status.GetStatusDurations
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.ComputeDurations
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import kotlin.time.DurationUnit


@SpringBootTest(classes = [GetStatusDurations::class])

class GetStatusDurationsTests {

    @Autowired
    private lateinit var getStatusDurations: GetStatusDurations

    @MockBean
    private lateinit var computeDurations: ComputeDurations

    private lateinit var defaultReturnValues: List<GetStatusDurations.ActionStatusWithDuration>

    private val missionStartDateTime = ZonedDateTime.of(2023, 6, 19, 10, 0, 0, 0, ZoneId.of("Europe/Berlin"))
    private val missionEndDateTime = ZonedDateTime.of(2023, 6, 30, 10, 0, 0, 0, ZoneId.of("Europe/Berlin"))

    @BeforeEach
    fun setUp() {
        defaultReturnValues = ActionStatusType.values().filter { it != ActionStatusType.UNKNOWN }.flatMap { status ->
            getStatusDurations.getSelectOptionsForType(status)?.map { reason ->
                GetStatusDurations.ActionStatusWithDuration(status, 0.0, reason)
            } ?: listOf(GetStatusDurations.ActionStatusWithDuration(status, 0.0))
        }
    }

    @Test
    fun `execute should return the default when statuses is null`() {
        val statuses = null
        val values = getStatusDurations.computeActionDurationsForAllMission(
            missionStartDateTime,
            missionEndDateTime,
            statuses
        );
        assertThat(values).containsExactlyInAnyOrder(*defaultReturnValues.toTypedArray())
    }

    @Test
    fun `execute should return the default when statuses is empty`() {
        val statuses = listOf<ActionStatusEntity>()
        val values = getStatusDurations.computeActionDurationsForAllMission(
            missionStartDateTime,
            missionEndDateTime,
            statuses
        );
        assertThat(values).containsExactlyInAnyOrder(*defaultReturnValues.toTypedArray())
    }

    @Test
    fun `execute should return duration for single status`() {
        val actionStatus = ActionStatusEntity(
            id = UUID.randomUUID(),
            missionId = 1,
            startDateTimeUtc = missionStartDateTime,
            status = ActionStatusType.NAVIGATING,
        )
        val statuses = listOf(actionStatus)


        val expectedDuration = Duration.between(actionStatus.startDateTimeUtc, missionEndDateTime).toSeconds()
        Mockito.`when`(computeDurations.durationInSeconds(actionStatus.startDateTimeUtc, missionEndDateTime))
            .thenReturn(expectedDuration.toInt())
        Mockito.`when`(computeDurations.convertFromSeconds(expectedDuration.toInt(), DurationUnit.SECONDS))
            .thenReturn(expectedDuration.toInt().toDouble())

        val values = getStatusDurations.computeActionDurationsForAllMission(
            missionStartDateTime,
            missionEndDateTime,
            statuses
        )

        assertThat(values).contains(
            GetStatusDurations.ActionStatusWithDuration(
                status = ActionStatusType.NAVIGATING,
                duration = expectedDuration.toDouble(),
                reason = null,
                date = missionStartDateTime.toLocalDate()
            )
        )
    }

//    @Test
//    fun `execute should return durations excluding last action when missionEndDateTime is null`() {
//        // Define the list of action status entities
//        val actions = listOf(
//            ActionStatusEntity(UUID.randomUUID(), 1, missionStartDateTime, ActionStatusType.NAVIGATING),
//            ActionStatusEntity(UUID.randomUUID(), 1, missionStartDateTime.plusHours(2), ActionStatusType.ANCHORED),
//            ActionStatusEntity(
//                UUID.randomUUID(),
//                1,
//                missionStartDateTime.plusHours(5),
//                ActionStatusType.DOCKED,
//                ActionStatusReason.WEATHER
//            ),
//            ActionStatusEntity(
//                UUID.randomUUID(),
//                1,
//                missionStartDateTime.plusHours(7),
//                ActionStatusType.UNAVAILABLE,
//                ActionStatusReason.TECHNICAL
//            )
//        )
//
//        // Call the computeActionDurations function with missionEndDateTime as null
//        val valuesInSeconds = getStatusDurations.computeActionDurationsForAllMission(
//            missionStartDateTime,
//            null,
//            actions
//        )
//        // Calculate expected durations in seconds excluding the last action
//        val expectedValues = listOf(
//            GetStatusDurations.ActionStatusWithDuration(ActionStatusType.NAVIGATING, 7200),
//            GetStatusDurations.ActionStatusWithDuration(ActionStatusType.ANCHORED, 18000),
//            GetStatusDurations.ActionStatusWithDuration(ActionStatusType.DOCKED, 18000, ActionStatusReason.WEATHER),
//            GetStatusDurations.ActionStatusWithDuration(ActionStatusType.UNAVAILABLE, 0, ActionStatusReason.TECHNICAL),
//        )
//        // Assert that the computed durations match the expected values
//        assertThat(valuesInSeconds).containsAll(expectedValues)
//
//        // same but with values in minutes
//        val valuesInMinutes = getStatusDurations.computeActionDurationsForAllMission(
//            missionStartDateTime,
//            null,
//            actions,
//            DurationUnit.MINUTES
//        )
//        val expectedValuesInMinutes = listOf(
//            GetStatusDurations.ActionStatusWithDuration(ActionStatusType.NAVIGATING, 120),
//            GetStatusDurations.ActionStatusWithDuration(ActionStatusType.ANCHORED, 300),
//            GetStatusDurations.ActionStatusWithDuration(ActionStatusType.DOCKED, 300, ActionStatusReason.WEATHER),
//        )
//        assertThat(valuesInMinutes).containsAll(expectedValuesInMinutes)
//
//        // same but with values in hours
//        val valuesInHours = getStatusDurations.computeActionDurationsForAllMission(
//            missionStartDateTime,
//            null,
//            actions,
//            DurationUnit.HOURS
//        )
//        val expectedValuesInHours = listOf(
//            GetStatusDurations.ActionStatusWithDuration(ActionStatusType.NAVIGATING, 2),
//            GetStatusDurations.ActionStatusWithDuration(ActionStatusType.ANCHORED, 5),
//            GetStatusDurations.ActionStatusWithDuration(ActionStatusType.DOCKED, 5, ActionStatusReason.WEATHER),
//        )
//        assertThat(valuesInHours).containsAll(expectedValuesInHours)
//    }

}
