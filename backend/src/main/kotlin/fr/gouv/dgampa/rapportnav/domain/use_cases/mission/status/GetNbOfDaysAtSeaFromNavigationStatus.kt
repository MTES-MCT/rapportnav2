package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatusEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import java.time.ZonedDateTime
import kotlin.time.DurationUnit

@UseCase
class GetNbOfDaysAtSeaFromNavigationStatus(
    private val getStatusDurations: GetStatusDurations,
) {

    /**
     * Computes the amount of days with min 4h of navigation
     * En français: un jour de mer est décompté dès que le navire effectue plus de 4 heures de navigation en mer.
     *
     * This function will get the duration for every Navigating status, group them by date,
     * check whether it exceeds 4 hours and sum it all up

     *
     * @param missionStartDateTime The start time of the mission.
     * @param missionEndDateTime The end time of the mission (optional). If provided, durations are calculated
     *     until this time; otherwise, durations are calculated until the current time for all but the last action.
     * @param actions The list of action status entities (optional). If not provided, default durations with zero
     *     values for all possible status and reason combinations are returned.
     * @param durationUnit which duration unit (seconds, minutes, hours)
     * @return An integer representing the amount of days with min 4h of navigation
     */
    fun execute(
        missionStartDateTime: ZonedDateTime,
        missionEndDateTime: ZonedDateTime? = null,
        actions: List<ActionStatusEntity>? = listOf(),
        durationUnit: DurationUnit = DurationUnit.HOURS
    ): Int {
        if (actions.isNullOrEmpty()) {
            return 0
        }
        val statusesWithDurations = getStatusDurations.computeActionDurationsPerAction(
            missionStartDateTime = missionStartDateTime,
            missionEndDateTime = missionEndDateTime,
            actions = actions,
            durationUnit = durationUnit
        ).filter { it.status === ActionStatusType.NAVIGATING }
        val actionsPerDay = statusesWithDurations.groupBy { it.date?.toLocalDate() }
        val exceedsFourHoursPerDay = actionsPerDay.mapValues { (_, statuses) ->
            statuses.sumOf { it.duration } > 4
        }
        val numberOfDaysExceedingFourHours = exceedsFourHoursPerDay.count { it.value }
        return numberOfDaysExceedingFourHours
    }

}
