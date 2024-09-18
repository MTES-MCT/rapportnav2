package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.PatchedEnvActionEntity
import java.time.Instant

data class PatchedEnvAction(
    val id: Any?,
    val startDateTimeUtc: Instant?,
    val endDateTimeUtc: Instant?,
    val observationsByUnit: String? = null,
) {
    companion object {
        fun fromPatchableEnvActionEntity(action: PatchedEnvActionEntity?): PatchedEnvAction? {
            return action?.let {
                PatchedEnvAction(
                    id = action.id,
                    startDateTimeUtc = action.actionStartDateTimeUtc,
                    endDateTimeUtc = action.actionEndDateTimeUtc,
                    observationsByUnit = action.observationsByUnit,
                )
            }
        }
    }

}
