package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.PatchEnvAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionDates
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.ProcessMissionActionTarget
import fr.gouv.dgampa.rapportnav.domain.validation.EntityValidityValidator
import fr.gouv.dgampa.rapportnav.domain.validation.ValidateThrowsBeforeSave
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.action.ActionEnvInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnvAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnvActionData

@UseCase
class UpdateEnvAction(
    private val patchEnvAction: PatchEnvAction,
    private val processMissionActionTarget: ProcessMissionActionTarget,
    private val entityValidityValidator: EntityValidityValidator,
    private val getMissionDates: GetMissionDates
) {
    fun execute(id: String, input: MissionEnvAction): MissionEnvActionEntity {
        val action = MissionEnvActionData.toMissionEnvActionEntity(input)

        entityValidityValidator.validateAndThrow(action, ValidateThrowsBeforeSave::class.java)

        patchEnvAction.execute(
            ActionEnvInput(
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
        // compute validity
        val missionDates = getMissionDates.execute(missionId = action.missionId, ownerId = null)
        action.computeValidity(isMissionFinished = missionDates?.isMissionFinished() ?: false)
        return action
    }
}
