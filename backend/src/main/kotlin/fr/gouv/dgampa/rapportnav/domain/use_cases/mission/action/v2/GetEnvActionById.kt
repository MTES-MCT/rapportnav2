package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IEnvMissionRepository
import org.slf4j.LoggerFactory

@UseCase
class GetEnvActionById(
    private val monitorEnvApiRepo: IEnvMissionRepository,
    private val attachControlToAction: AttachControlToAction
) {
    private val logger = LoggerFactory.getLogger(GetFishActionListByMissionId::class.java)

    fun execute(missionId: Int?, actionId: String): MissionEnvActionEntity? {
        if (missionId == null) {
            logger.error("GetEnvActionById received a null missionId")
            throw IllegalArgumentException("GetEnvActionById should not receive null missionId")
        }
        return try {
            val envAction = getEnvAction(missionId = missionId, actionId = actionId) ?: return null
            var actionEntity = MissionEnvActionEntity.fromEnvAction(missionId, envAction)
            actionEntity = attachControlToAction.execute(actionEntity) as MissionEnvActionEntity
            actionEntity.computeCompleteness()
            actionEntity
        } catch (e: Exception) {
            logger.error("GetEnvActionById failed loading Action", e)
            return null
        }
    }

    private fun getEnvAction(missionId: Int, actionId: String): EnvActionEntity? {
        return monitorEnvApiRepo.findMissionById(missionId = missionId)?.envActions?.find { it.id.toString() == actionId }
    }

}
