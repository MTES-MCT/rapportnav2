package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.ComputeDurations
import java.time.Duration
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
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
        missionStartDateTime: Instant? = null,
        missionEndDateTime: Instant? = null,
        actions: List<MissionNavActionEntity>? = listOf(),
        durationUnit: DurationUnit = DurationUnit.HOURS,
        zoneId: ZoneId = ZoneId.systemDefault()
    ): Int {
        if (actions.isNullOrEmpty() || missionStartDateTime == null) {
            return 0
        }

        // Filter valid statuses
        val validStatuses = listOf(ActionStatusType.NAVIGATING, ActionStatusType.ANCHORED)
        val durationThreshold = 4.0 // Minimum duration threshold in hours

        val statusesWithDurations = getStatusDurations.computeDurationsByAction(
            missionEndDateTime = missionEndDateTime,
            actions = actions,
            durationUnit = durationUnit
        )

        var count = 0
        var currentDay = missionStartDateTime.atZone(zoneId).toLocalDate()
        val lastDay = missionEndDateTime?.atZone(zoneId)?.toLocalDate()
            ?: missionStartDateTime.atZone(zoneId).toLocalDate()

        while (currentDay.isBefore(lastDay) || currentDay == lastDay) {
            val actionsForDay = statusesWithDurations.filter {
                it.datetime?.atZone(zoneId)?.toLocalDate()?.equals(currentDay) == true
            }
            val lastActionBeforeCurrent = statusesWithDurations.filter {
                it.datetime != null && it.datetime.atZone(zoneId).toLocalDate().isBefore(currentDay)
            }.maxByOrNull { it.datetime!! }

            // there is no action on that day
            // so we're checking if the action before is Navigating/Anchored
            // and if so, we will count that day
            if (actionsForDay.isEmpty()) {
                if (lastActionBeforeCurrent != null && validStatuses.contains(lastActionBeforeCurrent.status)) {
                    count += 1
                }
            } else {
                var countForDay = 0.0
                actionsForDay.forEachIndexed { index, action ->
                    action.datetime?.let { actionDateTime ->
                        if (index == 0) {
                            // count down to midnight and add if appropriate
                            if (lastActionBeforeCurrent != null && validStatuses.contains(lastActionBeforeCurrent.status)) {
                                val zonedDateTime = actionDateTime.atZone(zoneId)
                                val midnightOfAction = zonedDateTime.toLocalDate().atStartOfDay(zoneId).toInstant()
                                val durationUntilFirstStatus = Duration.between(
                                    midnightOfAction,
                                    actionDateTime
                                ).seconds.toInt()
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
                            val durationUntilEndOfDay = Duration.between(
                                actionDateTime,
                                actionDateTime.atZone(zoneId).plusDays(1).with(LocalTime.MIDNIGHT).toInstant()
                            ).seconds
                            val toHours =
                                computeDurations.convertFromSeconds(durationUntilEndOfDay.toInt(), DurationUnit.HOURS)
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
