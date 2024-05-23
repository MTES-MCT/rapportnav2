package fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.v1.adapters.outputs

import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity

data class ApiMissionDataOutput(
    val id: Int,
    val containsActionsAddedByUnit: Boolean
) {
    companion object {
        fun fromMissionEntity(mission: MissionEntity?): ApiMissionDataOutput? {
            return mission?.let { m ->
                ApiMissionDataOutput(
                    id = m.id,
                    containsActionsAddedByUnit = m.actions?.any { it is MissionActionEntity.NavAction } ?: false
                )
            }
        }

    }
}
