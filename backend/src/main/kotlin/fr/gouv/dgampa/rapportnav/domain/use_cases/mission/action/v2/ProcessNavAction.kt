package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionCrossControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel


@UseCase
class ProcessNavAction(
    getStatusForAction: GetStatusForAction2,
    private val getComputeTarget: GetComputeTarget,
    private val getComputeCrossControl: GetComputeCrossControl
) : AbstractGetMissionAction(getStatusForAction) {

    fun execute(missionId: Int?, action: MissionActionModel): MissionNavActionEntity {
        val entity = MissionNavActionEntity.fromMissionActionModel(action)
        entity.targets = getComputeTarget.execute(actionId = entity.getActionId(), isControl = entity.isControl())
        entity.crossControl = processMissionActionCrossControl(entity)
        // first complete the completeness
        entity.computeCompleteness()
        // then compute the derived status
        entity.status = this.getStatus(entity)
        return entity
    }

    fun processMissionActionCrossControl(action: MissionNavActionEntity): MissionActionCrossControlEntity? {
        val crossControl = getComputeCrossControl.execute(crossControlId = action.crossControl?.id)
        return action.crossControl?.fromCrossControlEntity(crossControl = crossControl)
    }
}
