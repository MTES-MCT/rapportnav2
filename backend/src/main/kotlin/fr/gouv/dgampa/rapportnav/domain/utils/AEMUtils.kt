package fr.gouv.dgampa.rapportnav.domain.utils

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.ActionEntity

class AEMUtils {
    companion object {

        fun getDurationInHours2(actions: List<ActionEntity?>): Double {
            return actions.fold(0.0) { acc, action ->
                acc.plus(
                    ComputeDurationUtils.durationInHours(
                        startDateTimeUtc = action?.startDateTimeUtc,
                        endDateTimeUtc = action?.endDateTimeUtc
                    )
                )
            }
        }
    }
}
