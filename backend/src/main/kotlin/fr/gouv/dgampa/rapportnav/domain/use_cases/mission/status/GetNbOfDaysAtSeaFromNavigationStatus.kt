package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatusEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.ComputeDurations
import java.time.LocalTime
import java.time.ZonedDateTime
import kotlin.time.DurationUnit

@UseCase
class GetNbOfDaysAtSeaFromNavigationStatus(
    private val getStatusDurations: GetStatusDurations,
    private val computeDurations: ComputeDurations
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

        // Filter valid statuses
        val validStatuses = listOf(ActionStatusType.NAVIGATING, ActionStatusType.ANCHORED)
        val durationThreshold = 4.0 // Minimum duration threshold in hours

        val statusesWithDurations = getStatusDurations.computeDurationsByAction(
            missionStartDateTime = missionStartDateTime,
            missionEndDateTime = missionEndDateTime,
            actions = actions,
            durationUnit = durationUnit
        )

        var count = 0
        var currentDay = missionStartDateTime.toLocalDate()
        val lastDay = missionEndDateTime?.toLocalDate() ?: missionStartDateTime.toLocalDate()
        while (currentDay.isBefore(lastDay) || currentDay == lastDay) {
            val actionsForDay = statusesWithDurations.filter { it.datetime?.toLocalDate()?.equals(currentDay) ?: false }
            val lastActionBeforeCurrent =
                statusesWithDurations.filter {
                    it.datetime != null && it.datetime.toLocalDate()?.isBefore(currentDay) == true
                }
                    .maxByOrNull { it.datetime!! }

            // there is no action on that day
            // so we're checking if the action before is Navigating/Anchored
            // and if so, we will count that day
            if (actionsForDay.isNullOrEmpty()) {
                if (lastActionBeforeCurrent != null && validStatuses.contains(lastActionBeforeCurrent.status)) {
                    count += 1
                }
            } else {
                var countForDay = 0.0
                actionsForDay.forEachIndexed { index, action ->
                    if (action.datetime != null) {
                        if (index == 0) {
                            // count down to midnight and add if appropriate
                            if (lastActionBeforeCurrent != null && action.datetime != null && validStatuses.contains(
                                    lastActionBeforeCurrent.status
                                )
                            ) {
                                val durationUntilFirstStatus = computeDurations.durationInSeconds(
                                    action.datetime.with(LocalTime.MIDNIGHT), action.datetime
                                ) ?: 0
                                val toHours =
                                    computeDurations.convertFromSeconds(durationUntilFirstStatus, DurationUnit.HOURS)
                                countForDay += toHours
                            }
                            // add the action duration if appropriate
                            if (validStatuses.contains(action.status)) {
                                countForDay += action.duration
                            }
                        } else if (index == actions.size - 1 && validStatuses.contains(action.status)) {
                            // count from action till end of day
                            val durationUntilEndOfDay = computeDurations.durationInSeconds(
                                action.datetime, action.datetime.plusDays(1L).with(LocalTime.MIDNIGHT)
                            ) ?: 0
                            val toHours =
                                computeDurations.convertFromSeconds(durationUntilEndOfDay, DurationUnit.HOURS)
                            countForDay += toHours
                        } else {
                            if (validStatuses.contains(action.status)) {
                                countForDay += action.duration
                            }
                        }
                    }
                }
                if (countForDay > durationThreshold) {
                    count += 1
                }
            }
            currentDay = currentDay.plusDays(1)
        }
        return count
    }

}
