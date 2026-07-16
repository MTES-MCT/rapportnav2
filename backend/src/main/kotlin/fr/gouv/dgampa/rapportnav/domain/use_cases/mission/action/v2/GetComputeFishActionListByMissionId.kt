package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionExternalId
import java.util.*

@UseCase
class GetComputeFishActionListByMissionId(
    private val processFishAction: ProcessFishAction,
    private val getFishActionListByMissionId: GetFishActionListByMissionId,
    private val getMissionExternalId: GetMissionExternalId
) {
    fun execute(missionId: UUID): List<MissionFishActionEntity> =
        execute(missionId, getMissionExternalId.execute(missionId))

    // Overload for callers that already resolved the external id, to avoid re-resolving it.
    fun execute(missionId: UUID, externalId: Int?): List<MissionFishActionEntity> {
        if (externalId == null) return emptyList()
        val actions = getFishActionList(externalId = externalId)
        return actions.map { processFishAction.execute(ownerId = missionId, action = it) }
    }

    private fun getFishActionList(externalId: Int): List<MissionAction> {
        return getFishActionListByMissionId.execute(missionId = externalId).filter {
            listOf(
                MissionActionType.SEA_CONTROL,
                MissionActionType.LAND_CONTROL
            ).contains(it.actionType)
        }
    }
}
