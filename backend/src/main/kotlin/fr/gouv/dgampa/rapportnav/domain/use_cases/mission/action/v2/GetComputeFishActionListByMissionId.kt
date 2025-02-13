package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2.GetControlByActionId2
import org.slf4j.LoggerFactory

@UseCase
class GetComputeFishActionListByMissionId(
    getStatusForAction: GetStatusForAction,
    getControlByActionId: GetControlByActionId2,
    private val getFishActionListByMissionId: GetFishActionListByMissionId
): AbstractGetMissionAction(getStatusForAction, getControlByActionId)  {
    private val logger = LoggerFactory.getLogger(GetComputeFishActionListByMissionId::class.java)

    fun execute(missionId: Int?): List<MissionFishActionEntity> {
        if (missionId == null) {
            logger.error("GetComputeFishActionListByMissionId received a null missionId")
            throw IllegalArgumentException("GetComputeFishActionListByMissionId" +
                " should not receive null missionId")
        }
        return try {
            val actions = getFishActionList(missionId = missionId)
            processActions(actions = actions)
        } catch (e: Exception) {
            logger.error("GetFishActionsByMissionId failed loading Actions", e)
            return listOf()
        }
    }


    private fun getFishActionList(missionId: Int): List<MissionAction> {
        return getFishActionListByMissionId.execute(missionId = missionId).orEmpty().filter {
            listOf(
                MissionActionType.SEA_CONTROL,
                MissionActionType.LAND_CONTROL
            ).contains(it.actionType)
        }
    }

    private fun processActions(actions: List<MissionAction>): List<MissionFishActionEntity> {
        return actions.map {
            val action = MissionFishActionEntity.fromFishAction(it)
            action.status = this.getStatus(action)
            action.computeControls(this.getControls(action))
            action.computeCompleteness()
            action
        }
    }
}
