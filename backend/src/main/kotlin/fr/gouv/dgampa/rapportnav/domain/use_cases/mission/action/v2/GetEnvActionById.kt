package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2.GetControlByActionId2
import org.slf4j.LoggerFactory

@UseCase
class GetEnvActionById(
    private val monitorEnvApiRepo: IEnvMissionRepository,
    getStatusForAction: GetStatusForAction,
    getControlByActionId: GetControlByActionId2,
): GetMissionAction(getStatusForAction, getControlByActionId)  {
    private val logger = LoggerFactory.getLogger(GetFishActionListByMissionId::class.java)

    fun execute(missionId: Int?, actionId: String): MissionEnvActionEntity? {
        if (missionId == null) {
            logger.error("GetEnvActionById received a null missionId")
            throw IllegalArgumentException("GetEnvActionById should not receive null missionId")
        }
        return try {
            val envAction = getEnvAction(missionId = missionId, actionId = actionId) ?: return null
            val entity = MissionEnvActionEntity.fromEnvAction(missionId, envAction)
            entity.computeControls(controls = this.getControls(entity))
            entity.computeCompleteness()
            entity
        } catch (e: Exception) {
            logger.error("GetEnvActionById failed loading Action", e)
            return null
        }
    }

    private fun getEnvAction(missionId: Int, actionId: String): EnvActionEntity? {
        return monitorEnvApiRepo.findMissionById(missionId = missionId)?.envActions?.find { it.id.toString() == actionId }
    }

}
