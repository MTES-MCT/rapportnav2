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
    private val missionNavRepository: IMissionNavRepository
) {
    fun execute(
        missionId: Int? = null,
        envMission: MissionEnvEntity? = null
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
        // Side-effect only: children are still read by the Int id below (no type switch in this step).
        syncLocalMission(externalId = id, env = mission)

        val actions = getMissionAction.execute(missionId = id)
        val generalInfos = getGeneralInfos2.execute(missionId = id, controlUnits = mission.controlUnits)

        return MissionEntity(
            id = id,
            data = mission,
            actions = actions,
            generalInfos = generalInfos
        )
    }

    /**
     * Looks up or creates the local mission row keyed by [externalId] (the MonitorEnv Int id),
     * reconciling the common fields from MonitorEnv.
     */
    private fun syncLocalMission(externalId: Int, env: MissionEnvEntity) {
        val existing = getMissionByExternalId.execute(externalId.toString())
        if (existing != null) {
            overrideFromEnv(existing, env)
        } else {
            missionNavRepository.save(
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
