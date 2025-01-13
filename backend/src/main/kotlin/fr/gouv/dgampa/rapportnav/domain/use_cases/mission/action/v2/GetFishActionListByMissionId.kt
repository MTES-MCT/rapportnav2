package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IFishActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.FakeActionData
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2.GetControlByActionId2
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable

@UseCase
class GetFishActionListByMissionId(
    private val fishActionRepo: IFishActionRepository,
    getStatusForAction: GetStatusForAction,
    getControlByActionId: GetControlByActionId2,
    private val getFakeActionData: FakeActionData
): GetMissionAction(getStatusForAction, getControlByActionId)  {
    private val logger = LoggerFactory.getLogger(GetFishActionListByMissionId::class.java)

    @Cacheable(value = ["fishActionList"], key = "#missionId")
    fun execute(missionId: Int?): List<MissionFishActionEntity> {
        if (missionId == null) {
            logger.error("GetFishListActionByMissionId received a null missionId")
            throw IllegalArgumentException("GetFishListActionByMissionId should not receive null missionId")
        }
        return try {
            val actions = getFishActionList(missionId = missionId)
            processActions(actions = actions)
        } catch (e: Exception) {
            logger.error("GetFishActionsByMissionId failed loading Actions", e)
            return listOf()
            //return fakeActions(missionId = missionId)
        }
    }

    private fun getFishActionList(missionId: Int): List<MissionAction> {
        return fishActionRepo.findFishActions(missionId = missionId).orEmpty().filter {
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

    private fun fakeActions(missionId: Int): List<MissionFishActionEntity> {
        val actions = getFakeActionData.getFakeFishActions(missionId)
        return processActions(actions = actions)
    }
}
