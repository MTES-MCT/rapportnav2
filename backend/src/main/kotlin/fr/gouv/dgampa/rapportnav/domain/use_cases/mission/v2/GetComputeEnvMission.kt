package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetEnvMissionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetMissionAction
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import java.util.*

@UseCase
class GetComputeEnvMission(
    private val getGeneralInfos2: GetGeneralInfo,
    private val getMissionAction: GetMissionAction,
    private val getEnvMissionById: GetEnvMissionById,
    private val getMissionByExternalId: GetMissionByExternalId,
    private val missionNavRepository: IMissionNavRepository,
    private val syncMissionValidation: SyncMissionValidation
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
        val localMission = syncLocalMission(externalId = id, env = mission)
        // NOTE: for now we only COLLECT the mission validation (persisted below via SyncMissionValidation),
        // we do not yet consume the stored status to short-circuit reads — validation runs every time, as
        // before. Re-enable the read shortcut by restoring the line below once the collected data is trusted.
        // val bypassValidation = !forceComputeValidation && localMission.isCompleteForStats == true
        val bypassValidation = false

        val actions = getMissionAction.execute(missionId = id, bypassValidation = bypassValidation)
        val generalInfos = getGeneralInfos2.execute(missionId = id, controlUnits = mission.controlUnits)

        val missionEntity = MissionEntity(
            id = id,
            data = mission,
            actions = actions,
            generalInfos = generalInfos
        )

        // Transition: persist the mission-level validation onto the (now-synced) mission row.
        syncMissionValidation.execute(missionEntity)

        return missionEntity
    }

    /**
     * Looks up or creates the local mission row keyed by [externalId] (the MonitorEnv Int id),
     * reconciling the common fields from MonitorEnv. Returns the resolved row (with its stored validation).
     */
    private fun syncLocalMission(externalId: Int, env: MissionEnvEntity): MissionModel {
        val existing = getMissionByExternalId.execute(externalId.toString())
        if (existing != null) {
            return overrideFromEnv(existing, env)
        }
        return missionNavRepository.save(
            MissionModel(
                id = UUID.randomUUID(),
                externalId = externalId.toString(),
                startDateTimeUtc = env.startDateTimeUtc,
                endDateTimeUtc = env.endDateTimeUtc,
                missionSource = env.missionSource,
                isDeleted = env.isDeleted ?: false,
                observationsByUnit = env.observationsByUnit,
                openBy = env.openBy,
                completedBy = env.completedBy
            )
        )
    }

    /**
     * Reconciles the local mission row with MonitorEnv (the source of truth) for the common
     * fields, persisting only when a value actually changed to avoid needless writes.
     */
    private fun overrideFromEnv(local: MissionModel, env: MissionEnvEntity): MissionModel {
        val hasChanged =
            local.startDateTimeUtc != env.startDateTimeUtc ||
                local.endDateTimeUtc != env.endDateTimeUtc ||
                local.missionSource != env.missionSource ||
                local.isDeleted != (env.isDeleted ?: false) ||
                local.observationsByUnit != env.observationsByUnit ||
                local.openBy != env.openBy ||
                local.completedBy != env.completedBy
        if (!hasChanged) return local

        local.startDateTimeUtc = env.startDateTimeUtc
        local.endDateTimeUtc = env.endDateTimeUtc
        local.missionSource = env.missionSource
        local.isDeleted = env.isDeleted ?: false
        local.observationsByUnit = env.observationsByUnit
        local.openBy = env.openBy
        local.completedBy = env.completedBy
        return missionNavRepository.save(local)
    }
}
