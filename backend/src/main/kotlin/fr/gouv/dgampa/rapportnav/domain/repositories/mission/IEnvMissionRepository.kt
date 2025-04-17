package fr.gouv.dgampa.rapportnav.domain.repositories.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ControlPlansEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.PatchedEnvActionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input.PatchActionInput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input.PatchMissionInput
import java.time.Instant

interface IEnvMissionRepository {

    fun findMissionById(missionId: String): MissionEntity?
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
    ): List<MissionEntity>?

    fun findAllControlPlans(): ControlPlansEntity?

    fun patchMission(missionId: String, mission: PatchMissionInput): MissionEntity?

    fun patchAction(actionId: String, action: PatchActionInput): PatchedEnvActionEntity?
}
