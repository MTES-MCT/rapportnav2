package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.ResourceUsageModel
import java.util.UUID

data class ResourceUsageEntity(
    var id: Int? = null,
    var missionIdUUID: UUID,
    var resourceId: Int,
    var nbKms: Double? = null,
    var nbEngineHours: Double? = null
) {

    companion object {
        fun fromResourceUsageModel(model: ResourceUsageModel): ResourceUsageEntity {
            return ResourceUsageEntity(
                id = model.id,
                missionIdUUID = model.missionIdUUID,
                resourceId = model.resourceId,
                nbKms = model.nbKms,
                nbEngineHours = model.nbEngineHours
            )
        }
    }

    fun toResourceUsageModel(): ResourceUsageModel {
        return ResourceUsageModel(
            id = id,
            missionIdUUID = missionIdUUID,
            resourceId = resourceId,
            nbKms = nbKms,
            nbEngineHours = nbEngineHours
        )
    }
}
