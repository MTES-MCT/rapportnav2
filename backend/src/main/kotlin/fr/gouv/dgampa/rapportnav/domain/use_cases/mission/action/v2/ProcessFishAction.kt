package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionDates
import fr.gouv.dgampa.rapportnav.domain.validation.EntityValidityValidator
import fr.gouv.dgampa.rapportnav.domain.validation.ValidationPolicies
import fr.gouv.dgampa.rapportnav.domain.validation.ValidationPolicy


@UseCase
class ProcessFishAction(
    getStatusForAction: GetStatusForAction,
    private val getComputeTarget: GetComputeTarget,
    private val getMissionDates: GetMissionDates,
    private val getComputeSati: GetComputeSati,
    private val entityValidityValidator: EntityValidityValidator
) : AbstractGetMissionAction(getStatusForAction) {

    /**
     * @param policy the pre-resolved validation policy. Callers processing a whole mission resolve it once and
     * pass it in to avoid an N+1 mission-dates lookup. When null, it is resolved from the mission's own dates.
     */
    fun execute(
        missionId: Int,
        action: MissionAction,
        bypassValidation: Boolean = false,
        policy: ValidationPolicy? = null
    ): MissionFishActionEntity {
        val entity = MissionFishActionEntity.fromFishAction(action)
        val sati = getComputeSati.execute(action = action)
        val targets = getComputeTarget.execute(actionId = entity.getActionId(), isControl = entity.isControl())

        entity.sati = sati
        entity.targets = targets

        if (bypassValidation) {
            entity.markCompleteForStats()
        } else {
            val resolvedPolicy = policy ?: ValidationPolicies.forMissionStartDate(
                getMissionDates.execute(missionId = missionId, ownerId = null)?.startDateTimeUtc
            )
            entity.computeValidity(validator = entityValidityValidator, policy = resolvedPolicy)
        }

        return entity
    }
}
