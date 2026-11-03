package fr.gouv.dgampa.rapportnav.domain.entities.satiExport

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.ControlCheck
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.GearControl

/**
 * Gear inspection of a sea control. MonitorFish answers its checks per gear on board:
 * the document sums them up for the whole control.
 */
data class SatiGearInspectionEntity(
    val gears: List<GearControl> = emptyList(),
) {
    val controlledGears: List<GearControl>
        get() = gears.filter { it.gearWasControlled == true }

    /** YES when at least one gear was controlled. */
    val gearControlled: ControlCheck
        get() = if (controlledGears.isNotEmpty()) ControlCheck.YES else ControlCheck.NO

    /** NO when a controlled gear has a non-compliant marking, YES when all of them are compliant, N/A otherwise. */
    val gearMarkingCompliant: ControlCheck
        get() {
            val markings = controlledGears.map { it.gearMarkingIsCompliant }
            return when {
                ControlCheck.NO in markings -> ControlCheck.NO
                markings.isNotEmpty() && markings.all { it == ControlCheck.YES } -> ControlCheck.YES
                else -> ControlCheck.NOT_APPLICABLE
            }
        }
}
