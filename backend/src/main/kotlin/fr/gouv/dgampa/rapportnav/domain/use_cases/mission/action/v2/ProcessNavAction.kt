package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.NavActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction

@UseCase
class ProcessNavAction(
    getStatusForAction: GetStatusForAction,
    private val getComputeTarget: GetComputeTarget
) : AbstractGetMissionAction(getStatusForAction) {

    fun execute(action: NavActionEntity): NavActionEntity {
        action.targets = getComputeTarget.execute(actionId = action.getActionId(), isControl = action.isControl())
        // compute completeness
        action.computeCompleteness()
        return action
    }
}
