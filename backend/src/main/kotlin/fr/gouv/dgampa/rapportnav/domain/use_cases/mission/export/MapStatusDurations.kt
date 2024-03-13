package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatusEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status.GetStatusDurations

@UseCase
class MapStatusDurations(
    private val getStatusDurations: GetStatusDurations,
) {

    private inline fun List<GetStatusDurations.ActionStatusWithDuration>.findDuration(predicate: (GetStatusDurations.ActionStatusWithDuration) -> Boolean): Int {
        return find(predicate)?.value?.toInt() ?: 0
    }

    fun execute(mission: MissionEntity, statuses: List<ActionStatusEntity>): Map<String, Map<String, Int>> {
        val durations = getStatusDurations.computeActionDurations(
            missionStartDateTime = mission.startDateTimeUtc,
            missionEndDateTime = mission.endDateTimeUtc,
            actions = statuses,
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