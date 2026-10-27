package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.generalInfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.PamMetricsMergeResult
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.PamResourceMetrics
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.ResourceUsageEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IResourceUsageRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.ResourceUsageModel
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
            dbRepo.findAllByMissionIdUUID(missionIdUUID).map { it.toResourceUsageEntity() }
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
                dbRepo.saveAll(usages.map { ResourceUsageModel.fromResourceUsageEntity(it) })
            }
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to save ResourceUsage for missionIdUUID='$missionIdUUID'",
                originalException = e
            )
        }
    }

    @Transactional
    override fun mergePamMetrics(
        missionIdUUID: UUID,
        resourceId: Int,
        metrics: PamResourceMetrics
    ): PamMetricsMergeResult {
        try {
            val existing = dbRepo.findByMissionIdUUIDAndResourceId(missionIdUUID, resourceId)
            val outcome = metrics.mergeOutcome(existing?.toResourceUsageEntity())
            if (outcome == PamMetricsMergeResult.CREATED) {
                dbRepo.save(ResourceUsageModel(missionIdUUID = missionIdUUID, resourceId = resourceId).withPamMetrics(metrics))
            }
            if (outcome == PamMetricsMergeResult.UPDATED && existing != null) {
                // Mutate the loaded row (rather than saving a rebuilt one) so the ULAM columns and audit fields are kept.
                dbRepo.save(existing.withPamMetrics(metrics))
            }
            return outcome
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to merge PAM metrics for missionIdUUID='$missionIdUUID', resourceId=$resourceId",
                originalException = e
            )
        }
    }

    private fun ResourceUsageModel.withPamMetrics(metrics: PamResourceMetrics): ResourceUsageModel = apply {
        distanceInNauticalMiles = metrics.distanceInNauticalMiles
        consumedGOInLiters = metrics.consumedGOInLiters
        consumedFuelInLiters = metrics.consumedFuelInLiters
    }
}
