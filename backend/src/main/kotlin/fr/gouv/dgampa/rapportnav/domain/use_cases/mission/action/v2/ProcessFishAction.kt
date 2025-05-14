package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction2


@UseCase
class ProcessFishAction(
    getStatusForAction: GetStatusForAction2,
    private val getComputeTarget: GetComputeTarget
) : AbstractGetMissionAction(getStatusForAction) {

    fun execute(missionId: Int, action: MissionAction): MissionFishActionEntity {
        val entity = MissionFishActionEntity.fromFishAction(action)
        val targets = getComputeTarget.execute(actionId = entity.getActionId(), isControl = entity.isControl())

        entity.targets = targets
        entity.status = this.getStatus(entity)
        entity.computeCompleteness()
        return entity
    }
}
