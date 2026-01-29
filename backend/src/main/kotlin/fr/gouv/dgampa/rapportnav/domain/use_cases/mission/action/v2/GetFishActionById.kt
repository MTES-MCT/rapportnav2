package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.FishAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.utils.isValidUUID

@UseCase
class GetFishActionById(
    private val processFishAction: ProcessFishAction,
    private val getFishActionListByMissionId: GetFishActionListByMissionId
) {
    fun execute(missionId: Int?, actionId: String?): MissionFishActionEntity? {
        if (missionId == null || actionId == null) return null
        if (!isInteger(actionId) || isValidUUID(actionId)) return null
        return try {
            val fishAction = getFishAction(missionId = missionId, actionId = actionId) ?: return null
            processFishAction.execute(missionId = missionId, action = fishAction)
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "GetFishActionById failed for missionId=$missionId, actionId=$actionId",
                originalException = e
            )
        }
    }

    private fun getFishAction(missionId: Int, actionId: String): FishAction? {
        return getFishActionListByMissionId.execute(missionId = missionId).find {
            it.id == actionId.toInt()
        }
    }

    private fun isInteger(actionId: String?): Boolean =
        actionId?.toIntOrNull() != null
}
