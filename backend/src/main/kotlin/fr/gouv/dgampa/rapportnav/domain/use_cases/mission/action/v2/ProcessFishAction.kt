package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2.GetControlByActionId2


@UseCase
class ProcessFishAction(
    getStatusForAction: GetStatusForAction,
    getControlByActionId: GetControlByActionId2,
    private val getComputeTarget: GetComputeTarget
) : AbstractGetMissionAction(getStatusForAction, getControlByActionId) {

    fun execute(missionId: String, action: MissionAction): MissionFishActionEntity {
        val entity = MissionFishActionEntity.fromFishAction(action)
        val targets = getComputeTarget.execute(actionId = entity.getActionId(), isControl = entity.isControl())

        entity.targets = targets
        entity.status = this.getStatus(entity)
        entity.computeControls(this.getControls(entity))
        entity.computeCompleteness()
        return entity
    }
}
