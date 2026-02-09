package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.FishActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction


@UseCase
class ProcessFishAction(
    getStatusForAction: GetStatusForAction,
    private val getComputeTarget: GetComputeTarget
) : AbstractGetMissionAction(getStatusForAction) {

    fun execute(missionId: Int, action: MissionAction): FishActionEntity {
        val entity = FishActionEntity.fromFishAction(action)
        val targets = getComputeTarget.execute(actionId = entity.getActionId(), isControl = entity.isControl())

        entity.targets = targets
        entity.status = this.getStatus(entity)
        entity.computeCompleteness()
        return entity
    }
}
