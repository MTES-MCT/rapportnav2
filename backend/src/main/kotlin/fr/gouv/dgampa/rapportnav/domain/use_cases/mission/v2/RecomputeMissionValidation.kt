package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import org.slf4j.LoggerFactory
import java.util.UUID

/**
 * Recomputes and re-persists a mission's validation after one of its actions changed.
 *
 * A mission's validity aggregates all of its actions, so an action create/update/delete can flip the
 * mission's stored validation. This rebuilds the full mission via the existing compute use cases (which
 * already call [SyncMissionValidation]) and lets them re-persist. Errors are swallowed/logged so a
 * recompute hiccup never breaks the action write that triggered it.
 */
@UseCase
class RecomputeMissionValidation(
    private val getComputeNavMission: GetComputeNavMission,
    private val getComputeEnvMission: GetComputeEnvMission,
) {
    private val logger = LoggerFactory.getLogger(RecomputeMissionValidation::class.java)

    /** For a RapportNav (nav) action: the mission is the local nav mission keyed by the action's ownerId. */
    fun forNavMission(ownerId: UUID?) {
        if (ownerId == null) return
        try {
            // forceComputeValidation: this is the write path, it must re-establish the real validity (never shortcut).
            getComputeNavMission.execute(missionId = ownerId, forceComputeValidation = true)
        } catch (e: Exception) {
            logger.warn("RecomputeMissionValidation - could not recompute nav mission {}", ownerId, e)
        }
    }

    /** For an env/fish action: the mission is the env mission keyed by its MonitorEnv external id. */
    fun forEnvMission(missionId: Int?) {
        if (missionId == null) return
        try {
            // forceComputeValidation: this is the write path, it must re-establish the real validity (never shortcut).
            getComputeEnvMission.execute(missionId = missionId, forceComputeValidation = true)
        } catch (e: Exception) {
            logger.warn("RecomputeMissionValidation - could not recompute env mission {}", missionId, e)
        }
    }
}
