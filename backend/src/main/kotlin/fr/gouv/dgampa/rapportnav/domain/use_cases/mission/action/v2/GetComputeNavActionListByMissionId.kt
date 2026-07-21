package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import java.util.*

@UseCase
class GetComputeNavActionListByMissionId(
    private val processNavAction: ProcessNavAction,
    private val getNavActionListByOwnerId: GetNavActionListByOwnerId
) {
    fun execute(missionId: Int, bypassValidation: Boolean = false): List<MissionNavActionEntity> {
        val actions = getNavActionListByOwnerId.execute(missionId = missionId)
        return actions.map { processNavAction.execute(action = it, bypassValidation = bypassValidation) }
    }

    fun execute(ownerId: UUID, bypassValidation: Boolean = false): List<MissionNavActionEntity> {
        val actions = getNavActionListByOwnerId.execute(ownerId = ownerId)
        return actions.map { processNavAction.execute(action = it, bypassValidation = bypassValidation) }
    }
}
