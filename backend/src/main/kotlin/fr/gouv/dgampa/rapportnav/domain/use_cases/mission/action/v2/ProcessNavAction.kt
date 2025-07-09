package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction2

@UseCase
class ProcessNavAction(
    getStatusForAction: GetStatusForAction2,
    private val getComputeTarget: GetComputeTarget
) : AbstractGetMissionAction(getStatusForAction) {

    fun execute(action: MissionNavActionEntity): MissionNavActionEntity {
        action.targets = getComputeTarget.execute(actionId = action.getActionId(), isControl = action.isControl())
        // compute completeness
        action.computeCompleteness()
        return action
    }
}
