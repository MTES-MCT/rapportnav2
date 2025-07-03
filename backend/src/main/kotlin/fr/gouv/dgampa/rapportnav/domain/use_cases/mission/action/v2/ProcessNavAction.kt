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

    fun execute(action: MissionActionModel): MissionNavActionEntity {
        val entity = MissionNavActionEntity.fromMissionActionModel(action)
        entity.targets = getComputeTarget.execute(actionId = entity.getActionId(), isControl = entity.isControl())
        // compute completeness
        entity.computeCompleteness()

        return entity
    }
}
