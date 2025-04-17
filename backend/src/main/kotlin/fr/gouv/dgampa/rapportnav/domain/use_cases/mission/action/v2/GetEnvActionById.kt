package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.utils.isValidUUID
import org.slf4j.LoggerFactory

@UseCase
class GetEnvActionById(
    private val getEnvMissionById2: GetEnvMissionById2,
    private val processEnvAction: ProcessEnvAction
) {
    private val logger = LoggerFactory.getLogger(GetFishActionListByMissionId::class.java)

    fun execute(missionId: String?, actionId: String): MissionEnvActionEntity? {
        if (!isValidUUID(actionId)) return null
        if (missionId == null) {
            logger.error("GetEnvActionById received a null missionId")
            throw IllegalArgumentException("GetEnvActionById should not receive null missionId")
        }
        return try {
            val envAction = getEnvAction(missionId = missionId, actionId = actionId) ?: return null
            processEnvAction.execute(missionId = missionId, envAction = envAction)
        } catch (e: Exception) {
            logger.error("GetEnvActionById failed loading Action", e)
            return null
        }
    }

    private fun getEnvAction(missionId: String, actionId: String): EnvActionEntity? {
        return getEnvMissionById2.execute(missionId = missionId)?.envActions?.find { it.id.toString() == actionId }
    }

}
