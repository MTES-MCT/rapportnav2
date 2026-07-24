package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.PatchFishAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.ProcessMissionActionTarget
import fr.gouv.dgampa.rapportnav.domain.validation.EntityValidityValidator
import fr.gouv.dgampa.rapportnav.domain.validation.ValidateThrowsBeforeSave
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.ProcessSati
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.action.ActionFishInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionFishAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionFishActionData

@UseCase
class UpdateFishAction(
    private val processSati: ProcessSati,
    private val patchFishAction: PatchFishAction,
    private val processMissionActionTarget: ProcessMissionActionTarget,
    private val entityValidityValidator: EntityValidityValidator,
    private val computeActionValidityAndRecomputeMission: ComputeActionValidityAndRecomputeMission
) {
    fun execute(id: String, input: MissionFishAction): MissionFishActionEntity {
        val action = MissionFishActionData.toMissionFishActionEntity(input)

        entityValidityValidator.validateAndThrow(action, ValidateThrowsBeforeSave::class.java)

        patchFishAction.execute(
            ActionFishInput(
                actionId = id,
                missionId = action.missionId,
                endDateTimeUtc = action.endDateTimeUtc,
                startDateTimeUtc = action.startDateTimeUtc,
                observationsByUnit = action.observationsByUnit,
                incidentDuringOperation = action.incidentDuringOperation,
                hasDivingDuringOperation = action.hasDivingDuringOperation
            )
        )
        action.sati = processSati.execute(actionId = action.getActionId(), sati = input.data.sati)
        action.targets = processMissionActionTarget.execute(
            actionId = action.getActionId(),
            targets = input.data.targets?.map { it.toTargetEntity() } ?: listOf()
        )

        // Computes the action validity and recomputes the env mission.
        computeActionValidityAndRecomputeMission.execute(action = action, ownerId = null)

        return action
    }
}
