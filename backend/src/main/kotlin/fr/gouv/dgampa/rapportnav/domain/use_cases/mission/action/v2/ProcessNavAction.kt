package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel


@UseCase
class ProcessNavAction(
    getStatusForAction: GetStatusForAction,
    private val getComputeTarget: GetComputeTarget
) : AbstractGetMissionAction(getStatusForAction) {

    fun execute(missionId: Int?, action: MissionActionModel): MissionNavActionEntity {
        val entity = MissionNavActionEntity.fromMissionActionModel(action)
        val targets = getComputeTarget.execute(actionId = entity.getActionId(), isControl = entity.isControl())
        entity.targets = targets
        entity.status = this.getStatus(entity)
        entity.computeCompleteness()
        return entity
    }
}
