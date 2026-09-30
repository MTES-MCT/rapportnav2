package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeNavActionListByMissionId
import java.util.*

@UseCase
class GetComputeNavMission(
    private val getGeneralInfo: GetGeneralInfo,
    private val getNavMissionById: GetNavMissionById,
    private val getComputeNavActionListByMissionId: GetComputeNavActionListByMissionId,
    private val syncMissionValidation: SyncMissionValidation
) {
    /**
     * @param forceComputeValidation set by the write/recompute path to always compute action validity for real. Read
     * callers omit it and get the shortcut: when the mission's stored completeness is already VALID, actions
     * are marked complete without re-running the per-field validation.
     */
    fun execute(missionId: UUID? = null, navMission: MissionNavEntity? = null, forceComputeValidation: Boolean = false): MissionEntity {
        if (missionId == null && navMission == null) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "Either missionId or navMission must be provided"
            )
        }

        val mission = navMission ?: getNavMissionById.execute(missionId!!)
            ?: throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "Nav mission not found: $missionId"
            )

        // Sticky completeness: a mission already known-complete is not re-validated on reads. When set, this
        // both marks the actions complete without per-field validation AND skips the mission-level persist
        // below — so an old complete mission is never retroactively downgraded.
        val bypassValidation = !forceComputeValidation && mission.isCompleteForStats == true

        val generalInfos = getGeneralInfo.execute(missionIdUUID = mission.id, serviceId = navMission?.serviceId)
        val actions = getComputeNavActionListByMissionId.execute(ownerId = mission.id, bypassValidation = bypassValidation)

        val baseMission = MissionEntity(
            idUUID = mission.id,
            actions = actions,
            generalInfos = generalInfos,
            data = MissionEnvEntity.fromMissionNavEntity(entity = mission)
        )
        // Sticky read: an already-complete mission (bypassValidation) stays complete without re-validation;
        // otherwise expose the freshly-computed status. Resolved here (not in the mappers) so every read
        // surface sees the same value.
        val missionEntity = baseMission.copy(
            completenessForStats = if (bypassValidation) CompletenessForStatsEntity.valid()
            else baseMission.isCompleteForStats()
        )

        // Persist the mission-level validation onto the mission row — but only on the force/write path or
        // when the mission is not yet known-complete. Skipping when bypassValidation is true keeps
        // completeness sticky: an already-complete mission is never downgraded on a read.
        if (!bypassValidation) {
            syncMissionValidation.execute(missionEntity)
        }

        return missionEntity
    }
}
