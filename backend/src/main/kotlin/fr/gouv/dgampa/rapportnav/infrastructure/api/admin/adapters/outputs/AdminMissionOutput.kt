package fr.gouv.dgampa.rapportnav.infrastructure.api.admin.adapters.outputs

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import java.time.Instant
import java.util.UUID

data class AdminMissionOutput(
    val id: UUID,
    val serviceId: Int?,
    val openBy: String?,
    val completedBy: String?,
    val externalId: String?,
    val startDateTimeUtc: Instant,
    val endDateTimeUtc: Instant?,
    val missionSource: MissionSourceEnum?,
    val isDeleted: Boolean,
    val isCompleteForStats: Boolean?,
    val sourcesOfMissingData: String?,
    val createdAt: Instant?,
    val updatedAt: Instant?
) {
    companion object {
        fun fromModel(model: MissionModel): AdminMissionOutput {
            return AdminMissionOutput(
                id = model.id,
                serviceId = model.serviceId,
                openBy = model.openBy,
                completedBy = model.completedBy,
                externalId = model.externalId,
                startDateTimeUtc = model.startDateTimeUtc,
                endDateTimeUtc = model.endDateTimeUtc,
                missionSource = model.missionSource,
                isDeleted = model.isDeleted,
                isCompleteForStats = model.isCompleteForStats,
                sourcesOfMissingData = model.sourcesOfMissingData,
                createdAt = model.createdAt,
                updatedAt = model.updatedAt
            )
        }
    }
}
