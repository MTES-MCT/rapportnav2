package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.generalInfo

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.ResourceUsageModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface IDBResourceUsageRepository : JpaRepository<ResourceUsageModel, Int> {
    fun findAllByMissionIdUUID(missionIdUUID: UUID): List<ResourceUsageModel>
    fun deleteAllByMissionIdUUID(missionIdUUID: UUID)
}
