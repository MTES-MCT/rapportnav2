package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionDates
import fr.gouv.dgampa.rapportnav.domain.validation.EntityValidityValidator
import fr.gouv.dgampa.rapportnav.domain.validation.ValidationPolicies
import fr.gouv.dgampa.rapportnav.domain.validation.ValidationPolicy


@UseCase
class ProcessEnvAction(
    getStatusForAction: GetStatusForAction,
    private val getComputeEnvTarget: GetComputeEnvTarget,
    private val getMissionDates: GetMissionDates,
    private val entityValidityValidator: EntityValidityValidator
) : AbstractGetMissionAction(getStatusForAction) {

    /**
     * @param policy the pre-resolved validation policy. Callers processing a whole mission resolve it once and
     * pass it in to avoid an N+1 mission-dates lookup. When null, it is resolved from the mission's own dates.
     */
    fun execute(
        missionId: Int,
        envAction: EnvActionEntity,
        bypassValidation: Boolean = false,
        policy: ValidationPolicy? = null
    ): MissionEnvActionEntity {
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
            val resolvedPolicy = policy ?: ValidationPolicies.forMissionStartDate(
                getMissionDates.execute(missionId = missionId, ownerId = null)?.startDateTimeUtc
            )
            action.computeValidity(validator = entityValidityValidator, policy = resolvedPolicy)
        }

        return action
    }
}
