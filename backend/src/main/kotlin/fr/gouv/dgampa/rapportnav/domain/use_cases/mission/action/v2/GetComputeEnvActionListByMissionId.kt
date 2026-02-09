package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.EnvActionEntity

@UseCase
class GetComputeEnvActionListByMissionId(
    private val getEnvMissionById2: GetEnvMissionById2,
    private val processEnvAction: ProcessEnvAction
) {
    fun execute(missionId: Int): List<EnvActionEntity> {
        val actions = getEnvActionList(missionId = missionId)
        return actions.filter { it.actionType !== ActionTypeEnum.NOTE }
            .map { processEnvAction.execute(missionId = missionId, envAction = it) }
    }

    private fun getEnvActionList(missionId: Int): List<EnvActionEntity> {
        return getEnvMissionById2.execute(missionId = missionId)?.envActions ?: listOf()
    }
}
