package fr.gouv.dgampa.rapportnav.infrastructure.api.admin.adapters.outputs

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import java.time.Instant
import java.util.UUID

data class AdminMissionActionOutput(
    val id: UUID,
    val ownerId: UUID?,
    val startDateTimeUtc: Instant,
    val endDateTimeUtc: Instant?,
    val actionType: ActionType,
    val status: String?,
    val isCompleteForStats: Boolean?
) {
    companion object {
        fun fromModel(model: MissionActionModel): AdminMissionActionOutput {
            return AdminMissionActionOutput(
                id = model.id,
                ownerId = model.ownerId,
                startDateTimeUtc = model.startDateTimeUtc,
                endDateTimeUtc = model.endDateTimeUtc,
                actionType = model.actionType,
                status = model.status,
                isCompleteForStats = model.isCompleteForStats
            )
        }
    }
}
