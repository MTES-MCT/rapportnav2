package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvMissionById2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetActionsByOwnerId
import java.util.*

@UseCase
class GetComputeEnvMission(
    private val getGeneralInfos2: GetGeneralInfo2,
    private val getActionsByOwnerId: GetActionsByOwnerId,
    private val getEnvMissionById2: GetEnvMissionById2,
    private val missionNavRepository: IMissionNavRepository
) {
    fun execute(
        externalId: Int? = null,
        envMission: MissionEnvEntity? = null
    ): MissionEntity {

        if (externalId == null && envMission == null) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "Either externalId or envMission must be provided"
            )
        }

        val mission = envMission ?: getEnvMissionById2.execute(externalId!!)
            ?: throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "Env mission not found: $externalId"
            )

        val envExternalId = mission.externalId
            ?: throw BackendInternalException(message = "Mission has no externalId")

        // Look up or create the local mission row, keeping MonitorEnv as source of truth
        val existing = missionNavRepository.findByExternalId(envExternalId.toString()).orElse(null)
        val localMission = if (existing != null) {
            overrideFromEnv(existing, mission)
        } else {
            missionNavRepository.save(
                MissionModel(
                    id = UUID.randomUUID(),
                    externalId = envExternalId.toString(),
                    startDateTimeUtc = mission.startDateTimeUtc,
                    endDateTimeUtc = mission.endDateTimeUtc,
                    missionSource = mission.missionSource,
                    isDeleted = mission.isDeleted ?: false,
                    observationsByUnit = mission.observationsByUnit,
                    openBy = mission.openBy,
                    completedBy = mission.completedBy
                )
            )
        }
        val missionUUID = localMission.id

        val actions = getActionsByOwnerId.execute(missionId = missionUUID)
        val generalInfos = getGeneralInfos2.execute(missionId = missionUUID, controlUnits = mission.controlUnits)

        return MissionEntity(
            id = missionUUID,
            externalId = envExternalId.toString(),
            data = mission,
            actions = actions,
            generalInfos = generalInfos
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
