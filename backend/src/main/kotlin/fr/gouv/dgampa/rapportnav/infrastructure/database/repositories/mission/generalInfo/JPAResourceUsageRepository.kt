package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.generalInfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.ResourceUsageEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IResourceUsageRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.generalInfo.IDBResourceUsageRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Repository
class JPAResourceUsageRepository(
    private val dbRepo: IDBResourceUsageRepository,
) : IResourceUsageRepository {

    override fun findByMissionId(missionIdUUID: UUID): List<ResourceUsageEntity> {
        return try {
            dbRepo.findAllByMissionIdUUID(missionIdUUID).map { ResourceUsageEntity.fromResourceUsageModel(it) }
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to find ResourceUsage for missionIdUUID='$missionIdUUID'",
                originalException = e
            )
        }
    }

    @Transactional
    override fun replaceForMission(missionIdUUID: UUID, usages: List<ResourceUsageEntity>) {
        try {
            // Delete-then-insert reconciliation: the bulk delete flushes before the inserts, so the
            // unique (mission_id_uuid, resource_id) constraint is never tripped mid-transaction.
            dbRepo.deleteAllByMissionIdUUID(missionIdUUID)
            dbRepo.flush()
            if (usages.isNotEmpty()) {
                dbRepo.saveAll(usages.map { it.toResourceUsageModel() })
            }
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to save ResourceUsage for missionIdUUID='$missionIdUUID'",
                originalException = e
            )
        }
    }
}
