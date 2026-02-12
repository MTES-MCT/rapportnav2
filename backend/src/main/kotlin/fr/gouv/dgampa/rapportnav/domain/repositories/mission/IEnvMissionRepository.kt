package fr.gouv.dgampa.rapportnav.domain.repositories.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.PatchedEnvActionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input.PatchActionInput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input.PatchMissionInput
import java.time.Instant

interface IEnvMissionRepository {

    fun findMissionById(missionId: Int): MissionEnvEntity?
    fun findAllMissions(
        pageNumber: Int? = null,
        pageSize: Int? = null,
        startedAfterDateTime: Instant? = null,
        startedBeforeDateTime: Instant? = null,
        missionSources: List<String>? = null,
        missionTypes: List<String>? = null,
        missionStatuses: List<String>? = null,
        seaFronts: List<String>? = null,
        controlUnits: List<Int>? = null
    ): List<MissionEnvEntity>?

    fun patchMission(missionId: Int, mission: PatchMissionInput): MissionEnvEntity?

    fun patchAction(actionId: String, action: PatchActionInput): PatchedEnvActionEntity?
}
