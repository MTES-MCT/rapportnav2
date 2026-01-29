package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ITargetRepository
import java.util.*

@UseCase
class DeleteTarget(
    private val targetRepo: ITargetRepository
) {
    fun execute(actionId: UUID?, actionType: ActionType?) {
        if (actionId == null || actionType !== ActionType.CONTROL) return
        try {
            targetRepo.deleteByActionId(actionId.toString())
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "DeleteTarget failed for actionId=$actionId",
                originalException = e
            )
        }
    }
}
