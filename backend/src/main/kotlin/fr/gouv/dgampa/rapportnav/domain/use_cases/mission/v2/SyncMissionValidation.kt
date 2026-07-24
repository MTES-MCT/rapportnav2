package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import org.slf4j.LoggerFactory

/**
 * Persists the mission-level validation (aggregated from its actions + env data + general info) onto the
 * `mission` row. Called from the mission compute/read paths, which build the full [MissionEntity].
 *
 * Writes only when the computed value differs from what's stored (mirrors GetComputeEnvMission's
 * overrideFromEnv), and swallows/logs errors so a sync hiccup never breaks a read. Remove these calls
 * once reads consume the stored value (Phase 2).
 */
@UseCase
class SyncMissionValidation(
    private val missionNavRepository: IMissionNavRepository,
    private val getMissionByExternalId: GetMissionByExternalId
) {
    private val logger = LoggerFactory.getLogger(SyncMissionValidation::class.java)

    fun execute(mission: MissionEntity) {
        try {
            val model = resolveRow(mission) ?: return
            val completeness = mission.isCompleteForStats()
            val isComplete = completeness.isComplete
            // Sort by name so the same set of sources always serializes identically — otherwise a mere
            // reordering across recomputes would trip the change-guard below and trigger a redundant save.
            val newSources = completeness.sources
                ?.takeIf { it.isNotEmpty() }
                ?.map { it.name }
                ?.sorted()
                ?.joinToString(",")

            if (model.isCompleteForStats != isComplete || model.sourcesOfMissingData != newSources) {
                model.isCompleteForStats = isComplete
                model.sourcesOfMissingData = newSources
                missionNavRepository.save(model)
            }
        } catch (e: Exception) {
            logger.warn("SyncMissionValidation - could not persist mission validation for {}", mission.idUUID ?: mission.id, e)
        }
    }

    /** Nav missions are keyed by their local UUID; env missions by their MonitorEnv external id. */
    private fun resolveRow(mission: MissionEntity): MissionModel? {
        mission.idUUID?.let { return missionNavRepository.findById(it).orElse(null) }
        return mission.id?.let { getMissionByExternalId.execute(it.toString()) }
    }
}
