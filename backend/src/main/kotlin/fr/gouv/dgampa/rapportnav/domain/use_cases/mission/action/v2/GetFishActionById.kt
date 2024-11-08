package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.FishAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IFishActionRepository
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable

@UseCase
class GetFishActionById(
    private val fishActionRepo: IFishActionRepository,
    private val attachControlToAction: AttachControlToAction,
) {
    private val logger = LoggerFactory.getLogger(GetFishActionListByMissionId::class.java)

    @Cacheable(value = ["fishActionList"], key = "#missionId")
    fun execute(missionId: Int?, actionId: String?): MissionFishActionEntity? {
        if (missionId == null || actionId == null) {
            logger.error("GetFishActionById received a null missionId or actionId null")
            throw IllegalArgumentException("GetFishActionById should not receive null missionId or actionId null")
        }
        return try {
            val fishAction = getFishAction(missionId = missionId, actionId = actionId) ?: return null
            var actionEntity = MissionFishActionEntity.fromFishAction(fishAction)
            actionEntity = attachControlToAction.execute(actionEntity) as MissionFishActionEntity
            actionEntity.computeCompleteness()
            actionEntity
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
}
