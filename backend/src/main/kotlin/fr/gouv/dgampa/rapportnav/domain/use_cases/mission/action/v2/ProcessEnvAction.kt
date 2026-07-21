package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionDates
import fr.gouv.dgampa.rapportnav.domain.validation.EntityValidityValidator
import fr.gouv.dgampa.rapportnav.domain.validation.ValidationPolicies


@UseCase
class ProcessEnvAction(
    getStatusForAction: GetStatusForAction,
    private val getComputeEnvTarget: GetComputeEnvTarget,
    private val getMissionDates: GetMissionDates,
    private val entityValidityValidator: EntityValidityValidator
) : AbstractGetMissionAction(getStatusForAction) {

    fun execute(missionId: Int, envAction: EnvActionEntity, bypassValidation: Boolean = false): MissionEnvActionEntity {
        val action = MissionEnvActionEntity.fromEnvAction(missionId = missionId, action = envAction)
        val targets = getComputeEnvTarget.execute(
            actionId = action.getActionId(),
            isControl = action.isControl(),
            envInfractions = action.envInfractions,
        )

        action.targets = targets

        if (bypassValidation) {
            action.markCompleteForStats()
        } else {
            val missionDates = getMissionDates.execute(missionId = missionId, ownerId = null)
            val policy = ValidationPolicies.forMissionStartDate(missionDates?.startDateTimeUtc)
            action.computeValidity(validator = entityValidityValidator, policy = policy)
        }

        return action
    }
}
