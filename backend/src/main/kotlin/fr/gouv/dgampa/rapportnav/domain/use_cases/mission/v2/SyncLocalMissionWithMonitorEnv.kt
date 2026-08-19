package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import java.util.*

/**
 * Looks up or creates the local `mission` row that mirrors a MonitorEnv mission (the source of truth),
 * keyed by its MonitorEnv Int id (`externalId`), and reconciles the common fields. Returns the resolved
 * row — which carries the persisted validation columns (`isCompleteForStats` / `sourcesOfMissingData`).
 *
 * Extracted from [GetComputeEnvMission] so the light list path ([GetMissionList]) can keep the env↔local
 * mirror in sync and read completeness without duplicating the reconciliation logic.
 */
@UseCase
class SyncLocalMissionWithMonitorEnv(
    private val getMissionByExternalId: GetMissionByExternalId,
    private val missionNavRepository: IMissionNavRepository,
) {
    fun execute(externalId: Int, env: MissionEnvEntity): MissionModel {
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
