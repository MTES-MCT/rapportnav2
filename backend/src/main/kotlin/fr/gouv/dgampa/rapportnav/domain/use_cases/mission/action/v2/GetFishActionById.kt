package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.FishAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IFishActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2.GetControlByActionId2
import fr.gouv.dgampa.rapportnav.domain.utils.isValidUUID
import org.slf4j.LoggerFactory

@UseCase
class GetFishActionById(
    private val fishActionRepo: IFishActionRepository,
    getStatusForAction: GetStatusForAction,
    getControlByActionId: GetControlByActionId2,
): AbstractGetMissionAction(getStatusForAction, getControlByActionId)  {
    private val logger = LoggerFactory.getLogger(GetFishActionListByMissionId::class.java)

    fun execute(missionId: Int?, actionId: String?): MissionFishActionEntity? {
        if (!isInteger(actionId) || isValidUUID(actionId))  return null
        if (missionId == null || actionId == null) {
            logger.error("GetFishActionById received a null missionId or actionId null")
            throw IllegalArgumentException("GetFishActionById should not receive null missionId or actionId null")
        }
        logger.info("Valid actionId $actionId")
        return try {
            val fishAction = getFishAction(missionId = missionId, actionId = actionId) ?: return null
            logger.info("Successfully got fish action $fishAction")
            val entity = MissionFishActionEntity.fromFishAction(fishAction)
            entity.computeControls(controls = this.getControls(entity))
            entity.computeCompleteness()
            logger.info("Successfully got fish action $entity")
            entity
        } catch (e: Exception) {
            logger.error("GetFishActionsByMissionId failed loading Actions", e)
            return null
        }
    }

    private fun getFishAction(missionId: Int, actionId: String?): FishAction? {
        return fishActionRepo.findFishActions(missionId = missionId).find {
            it.id == Integer.valueOf(actionId)
        }
    }

    private fun isInteger(actionId: String?): Boolean =
        actionId?.let { runCatching { Integer.valueOf(actionId) }.isSuccess } == true
}
