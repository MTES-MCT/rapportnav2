package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ResourceUsageKind
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.usageKind
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.ResourceUsageEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IResourceUsageRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetEnvMissionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetMissionAction

@UseCase
class GetComputeEnvMission(
    private val getGeneralInfos2: GetGeneralInfo,
    private val getMissionAction: GetMissionAction,
    private val getEnvMissionById: GetEnvMissionById,
    private val syncMissionValidation: SyncMissionValidation,
    private val resourceUsageRepository: IResourceUsageRepository,
    private val syncLocalMissionWithMonitorEnv: SyncLocalMissionWithMonitorEnv,
) {
    /**
     * @param forceComputeValidation set by the write/recompute path to always compute action validity for real. Read
     * callers omit it and get the shortcut: when the mission row's stored completeness is already VALID,
     * actions are marked complete without re-running the per-field validation.
     */
    fun execute(
        missionId: Int? = null,
        envMission: MissionEnvEntity? = null,
        forceComputeValidation: Boolean = false
    ): MissionEntity {

        if (missionId == null && envMission == null) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "Either missionId or envMission must be provided"
            )
        }

        val mission = envMission ?: getEnvMissionById.execute(missionId!!)
            ?: throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "Env mission not found: $missionId"
            )

        val id = mission.id
            ?: throw BackendInternalException(message = "Mission has no id")

        // Prepopulate & keep the local mission row in sync with MonitorEnv (the source of truth).
        val localMission = syncLocalMissionWithMonitorEnv.execute(externalId = id, env = mission)
        // Sticky read: a mission already known-complete is not re-validated on reads. When set, this both
        // marks the actions complete without per-field validation AND skips the mission-level persist below —
        // so an old complete mission is never retroactively downgraded (e.g. by newer validation rules).
        val bypassValidation = !forceComputeValidation && localMission.isCompleteForStats == true

        val actions = getMissionAction.execute(missionId = id, bypassValidation = bypassValidation)
        val generalInfos = getGeneralInfos2.execute(missionId = id, controlUnits = mission.controlUnits)

        // Stitch the per-resource usage values (stored in our DB, keyed by the mission UUID) back onto the
        // MonitorEnv resources by resourceId, so both the frontend and the completeness check see them.
        val usages = resourceUsageRepository.findByMissionId(localMission.id)
        val enrichedMission = enrichResourcesWithUsage(mission, usages)

        val baseMission = MissionEntity(
            id = id,
            data = enrichedMission,
            actions = actions,
            generalInfos = generalInfos
        )
        // Sticky read: an already-complete mission (bypassValidation) stays complete without re-validation;
        // otherwise expose the freshly-computed status. Resolved here (not in the mappers) so every read
        // surface sees the same value.
        val missionEntity = baseMission.copy(
            completenessForStats = if (bypassValidation) CompletenessForStatsEntity.valid()
            else baseMission.isCompleteForStats()
        )

        // Persist the mission-level validation onto the (now-synced) mission row — but only on the
        // force/write path or when the mission is not yet known-complete. Skipping when bypassValidation is
        // true keeps completeness sticky: an already-complete mission is never downgraded on a read.
        // SyncMissionValidation recomputes the fresh value itself, so the resolved entity is safe to pass.
        if (!bypassValidation) {
            syncMissionValidation.execute(missionEntity)
        }

        return missionEntity
    }

    /**
     * Joins the stored usage rows onto the currently-selected MonitorEnv resources by resourceId.
     * Defends against MonitorEnv changing a resource's type: a stored value is only kept when it still
     * matches the resource's current [usageKind]. Usage rows whose resource is no longer selected are
     * simply never joined (dropped).
     */
    private fun enrichResourcesWithUsage(
        mission: MissionEnvEntity,
        usages: List<ResourceUsageEntity>?
    ): MissionEnvEntity {
        if (usages.isNullOrEmpty()) return mission
        val usageByResourceId = usages.associateBy { it.resourceId }
        return mission.copy(
            controlUnits = mission.controlUnits.map { controlUnit ->
                controlUnit.copy(
                    resources = controlUnit.resources?.map { resource ->
                        val usage = usageByResourceId[resource.id]
                        resource.copy(
                            nbKms = usage?.nbKms?.takeIf { resource.type?.usageKind() == ResourceUsageKind.KM },
                            nbEngineHours = usage?.nbEngineHours
                                ?.takeIf { resource.type?.usageKind() == ResourceUsageKind.ENGINE_HOURS }
                        )
                    }?.toMutableList()
                )
            }
        )
    }
}
