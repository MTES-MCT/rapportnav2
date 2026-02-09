package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.FishActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.PatchFishAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.ProcessMissionActionTarget
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.action.ActionFishInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionFishAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionFishActionData

@UseCase
class UpdateFishAction(
    private val patchFishAction: PatchFishAction,
    private val processMissionActionTarget: ProcessMissionActionTarget
) {
    fun execute(id: String, input: MissionFishAction): FishActionEntity {
        val action = MissionFishActionData.toMissionFishActionEntity(input)
        patchFishAction.execute(
            ActionFishInput(
                actionId = id,
                missionId = action.missionId,
                startDateTimeUtc = action.startDateTimeUtc,
                endDateTimeUtc = action.endDateTimeUtc,
                observationsByUnit = action.observationsByUnit
            )
        )
        action.targets = processMissionActionTarget.execute(
            actionId = action.getActionId(),
            targets = input.data.targets?.map { it.toTargetEntity() } ?: listOf()
        )
        action.computeCompleteness()
        return action
    }
}
