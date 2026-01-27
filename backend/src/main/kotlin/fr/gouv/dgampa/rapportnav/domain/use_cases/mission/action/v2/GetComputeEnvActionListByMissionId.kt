package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import org.slf4j.LoggerFactory

@UseCase
class GetComputeEnvActionListByMissionId(
    private val getEnvMissionById2: GetEnvMissionById2,
    private val processEnvAction: ProcessEnvAction
) {
    private val logger = LoggerFactory.getLogger(GetComputeEnvActionListByMissionId::class.java)

    fun execute(missionId: Int): List<MissionEnvActionEntity> {
        return try {
            val actions = getEnvActionList(missionId = missionId)
            actions.filter { it.actionType !== ActionTypeEnum.NOTE }
                .map {
                processEnvAction.execute(missionId = missionId, envAction = it)
            }
        } catch (e: Exception) {
            logger.error("GetComputeEnvActionListByMissionId failed loading actions for missionId=$missionId", e)
            emptyList()
        }
    }

    private fun getEnvActionList(missionId: Int): List<EnvActionEntity> {
        return getEnvMissionById2.execute(missionId = missionId)?.envActions ?: listOf()
    }
}
