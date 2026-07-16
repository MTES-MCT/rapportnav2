package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.PatchEnvAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionExternalId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.ProcessMissionActionTarget
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionDates
import fr.gouv.dgampa.rapportnav.domain.validation.EntityValidityValidator
import fr.gouv.dgampa.rapportnav.domain.validation.ValidateThrowsBeforeSave
import fr.gouv.dgampa.rapportnav.domain.validation.ValidationPolicies
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.action.ActionEnvInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnvAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnvActionData
import java.util.*

@UseCase
class UpdateEnvAction(
    private val patchEnvAction: PatchEnvAction,
    private val processMissionActionTarget: ProcessMissionActionTarget,
    private val entityValidityValidator: EntityValidityValidator,
    private val getMissionDates: GetMissionDates,
    private val getMissionExternalId: GetMissionExternalId
) {
    fun execute(id: String, input: MissionEnvAction): MissionEnvActionEntity {
        val action = MissionEnvActionData.toMissionEnvActionEntity(input)

        entityValidityValidator.validateAndThrow(action, ValidateThrowsBeforeSave::class.java)

        val ownerId = input.ownerId

        patchEnvAction.execute(
            ActionEnvInput(
                actionId = id,
                missionId = ownerId,
                externalId = getMissionExternalId.execute(ownerId),
                startDateTimeUtc = action.startDateTimeUtc,
                endDateTimeUtc = action.endDateTimeUtc,
                observationsByUnit = action.observationsByUnit,
                hasDivingDuringOperation = action.hasDivingDuringOperation,
                incidentDuringOperation = action.incidentDuringOperation,
            )
        )
        action.targets = processMissionActionTarget.execute(
            actionId = action.getActionId(),
            targets = input.data.targets?.map { it.toTargetEntity() } ?: listOf()
        )

        val missionDates = getMissionDates.execute(missionId = ownerId)
        val policy = ValidationPolicies.forMissionStartDate(missionDates?.startDateTimeUtc)
        action.computeValidity(validator = entityValidityValidator, policy = policy)
        return action
    }
}
