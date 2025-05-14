package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel


@UseCase
class ProcessNavAction(
    getStatusForAction: GetStatusForAction2,
    private val getComputeTarget: GetComputeTarget
) : AbstractGetMissionAction(getStatusForAction) {

    fun execute(missionId: Int?, action: MissionActionModel): MissionNavActionEntity {
        val entity = MissionNavActionEntity.fromMissionActionModel(action)
        val targets = getComputeTarget.execute(actionId = entity.getActionId(), isControl = entity.isControl())
        entity.targets = targets
        // first complete the completeness
        entity.computeCompleteness()
        // then compute the derived status
        entity.status = this.getStatus(entity)
        return entity
    }
}
