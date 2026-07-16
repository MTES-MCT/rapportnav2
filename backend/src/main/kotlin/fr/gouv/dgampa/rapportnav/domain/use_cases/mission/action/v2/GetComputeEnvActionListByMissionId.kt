package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionExternalId
import java.util.*

@UseCase
class GetComputeEnvActionListByMissionId(
    private val getEnvMissionById2: GetEnvMissionById2,
    private val processEnvAction: ProcessEnvAction,
    private val getMissionExternalId: GetMissionExternalId
) {
    fun execute(missionId: UUID): List<MissionEnvActionEntity> =
        execute(missionId, getMissionExternalId.execute(missionId))

    // Overload for callers that already resolved the external id, to avoid re-resolving it.
    fun execute(missionId: UUID, externalId: Int?): List<MissionEnvActionEntity> {
        if (externalId == null) return emptyList()
        val actions = getEnvActionList(externalId = externalId)
        return actions.filter { it.actionType !== ActionTypeEnum.NOTE }
            .map { processEnvAction.execute(ownerId = missionId, envAction = it) }
    }

    private fun getEnvActionList(externalId: Int): List<EnvActionEntity> {
        return getEnvMissionById2.execute(missionId = externalId)?.envActions ?: listOf()
    }
}
