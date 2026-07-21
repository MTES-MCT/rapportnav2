package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
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

        // NOTE: for now we only COLLECT the mission validation (persisted below via SyncMissionValidation),
        // we do not yet consume the stored status to short-circuit reads — validation runs every time, as
        // before. Re-enable the read shortcut by restoring the line below once the collected data is trusted.
        // val bypassValidation = !forceComputeValidation && mission.isCompleteForStats == true
        val bypassValidation = false

        val generalInfos = getGeneralInfo.execute(missionIdUUID = mission.id, serviceId = navMission?.serviceId)
        val actions = getComputeNavActionListByMissionId.execute(ownerId = mission.id, bypassValidation = bypassValidation)

        val missionEntity = MissionEntity(
            idUUID = mission.id,
            actions = actions,
            generalInfos = generalInfos,
            data = MissionEnvEntity.fromMissionNavEntity(entity = mission)
        )

        // Transition: persist the mission-level validation onto the mission row.
        syncMissionValidation.execute(missionEntity)

        return missionEntity
    }
}
