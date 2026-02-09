package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.FishActionEntity

@UseCase
class GetComputeFishActionListByMissionId(
    private val processFishAction: ProcessFishAction,
    private val getFishActionListByMissionId: GetFishActionListByMissionId
) {
    fun execute(missionId: Int): List<FishActionEntity> {
        val actions = getFishActionList(missionId = missionId)
        return actions.map { processFishAction.execute(missionId = missionId, action = it) }
    }

    private fun getFishActionList(missionId: Int): List<MissionAction> {
        return getFishActionListByMissionId.execute(missionId = missionId).filter {
            listOf(
                MissionActionType.SEA_CONTROL,
                MissionActionType.LAND_CONTROL
            ).contains(it.actionType)
        }
    }
}
