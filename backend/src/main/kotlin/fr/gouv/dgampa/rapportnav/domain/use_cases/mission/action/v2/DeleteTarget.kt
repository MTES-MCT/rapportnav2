package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ITargetRepository
import java.util.*

@UseCase
class DeleteTarget(
    private val targetRepo: ITargetRepository
) {
    fun execute(actionId: UUID?, actionType: ActionType?) {
        if (actionId == null || actionType !== ActionType.CONTROL) return
        return targetRepo.deleteByActionId(actionId.toString())
    }
}
