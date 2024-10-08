package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import java.time.Instant

data class PatchedFishAction(
    val id: Any?,
    val startDateTimeUtc: Instant?,
    val endDateTimeUtc: Instant?,
    val observationsByUnit: String? = null,
) {
    companion object {
        fun fromMissionAction(action: MissionAction?): PatchedFishAction? {
            return action?.let {
                PatchedFishAction(
                    id = action.id,
                    startDateTimeUtc = action.actionDatetimeUtc,
                    endDateTimeUtc = action.actionEndDatetimeUtc,
                    observationsByUnit = action.observationsByUnit,
                )
            }
        }
    }

}
