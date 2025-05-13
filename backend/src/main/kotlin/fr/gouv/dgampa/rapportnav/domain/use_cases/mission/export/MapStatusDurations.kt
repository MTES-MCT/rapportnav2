package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatusEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status.GetStatusDurations
import kotlin.math.ceil
import kotlin.time.DurationUnit

@UseCase
class MapStatusDurations(
    private val getStatusDurations: GetStatusDurations,
) {

    private inline fun List<GetStatusDurations.ActionStatusWithDuration>.findDuration(predicate: (GetStatusDurations.ActionStatusWithDuration) -> Boolean): Int {
        return ceil(find(predicate)?.duration ?: 0.0).toInt()
    }

    fun execute(
        mission: MissionEntity,
        statuses: List<ActionStatusEntity>,
        durationUnit: DurationUnit = DurationUnit.SECONDS
    ): Map<String, Map<String, Int>> {
        val durations = getStatusDurations.computeActionDurationsForAllMission(
            missionStartDateTime = mission.startDateTimeUtc,
            missionEndDateTime = mission.endDateTimeUtc,
            actions = statuses,
            durationUnit = durationUnit
        )

        val atSeaDurations = mapOf(
            "navigationEffective" to durations.findDuration { it.status == ActionStatusType.NAVIGATING },
            "mouillage" to durations.findDuration { it.status == ActionStatusType.ANCHORED },
            "total" to 0
        ).toMutableMap()
        atSeaDurations["total"] = atSeaDurations.values.sum()

        val dockingDurations = mapOf(
            "maintenance" to durations.findDuration { it.reason == ActionStatusReason.MAINTENANCE },
            "meteo" to durations.findDuration { it.reason == ActionStatusReason.WEATHER },
            "representation" to durations.findDuration { it.reason == ActionStatusReason.REPRESENTATION },
            "adminFormation" to durations.findDuration { it.reason == ActionStatusReason.ADMINISTRATION },
            "autre" to durations.findDuration { it.reason == ActionStatusReason.OTHER },
            "contrPol" to durations.findDuration { it.reason == ActionStatusReason.HARBOUR_CONTROL },
            "mco" to durations.findDuration { it.reason == ActionStatusReason.MCO_AND_LOGISTICS },
            "total" to 0
        ).toMutableMap()
        dockingDurations["total"] = dockingDurations.values.sum()

        val unavailabilityDurations = mapOf(
            "technique" to durations.findDuration { it.reason == ActionStatusReason.TECHNICAL },
            "personnel" to durations.findDuration { it.reason == ActionStatusReason.PERSONNEL },
            "total" to 0
        ).toMutableMap()
        unavailabilityDurations["total"] = unavailabilityDurations.values.sum()

        return mapOf(
            "atSeaDurations" to atSeaDurations.toMap(),
            "dockingDurations" to dockingDurations.toMap(),
            "unavailabilityDurations" to unavailabilityDurations.toMap()
        )
    }
}
