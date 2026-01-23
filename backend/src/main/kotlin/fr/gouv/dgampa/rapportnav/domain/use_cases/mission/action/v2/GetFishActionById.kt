package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.FishAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.utils.isValidUUID

@UseCase
class GetFishActionById(
    private val processFishAction: ProcessFishAction,
    private val getFishActionListByMissionId: GetFishActionListByMissionId
) {
    fun execute(missionId: Int?, actionId: String?): MissionFishActionEntity? {
        if (!isInteger(actionId) || isValidUUID(actionId)) return null
        if (missionId == null || actionId == null) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "GetFishActionById: missionId and actionId are required"
            )
        }
        val fishAction = getFishAction(missionId = missionId, actionId = actionId) ?: return null
        return processFishAction.execute(missionId = missionId, action = fishAction)
    }

    private fun getFishAction(missionId: Int, actionId: String?): FishAction? {
        return getFishActionListByMissionId.execute(missionId = missionId).find {
            it.id == Integer.valueOf(actionId)
        }
    }

    private fun isInteger(actionId: String?): Boolean =
        actionId?.let { runCatching { Integer.valueOf(actionId) }.isSuccess } == true
}
