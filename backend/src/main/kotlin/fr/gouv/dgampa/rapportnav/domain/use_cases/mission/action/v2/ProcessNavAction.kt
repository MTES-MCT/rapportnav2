package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2.GetControlByActionId2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel


@UseCase
class ProcessNavAction(
    getStatusForAction: GetStatusForAction,
    getControlByActionId: GetControlByActionId2,
    private val getComputeTarget: GetComputeTarget
) : AbstractGetMissionAction(getStatusForAction, getControlByActionId) {

    fun execute(missionId: Int?, action: MissionActionModel): MissionNavActionEntity {
        val entity = MissionNavActionEntity.fromMissionActionModel(action)
        val targets = getComputeTarget.execute(actionId = entity.getActionId(), isControl = entity.isControl())
        entity.targets = targets
        entity.status = this.getStatus(entity)
        entity.computeControls(this.getControls(entity))
        entity.computeCompleteness()
        return entity
    }
}
