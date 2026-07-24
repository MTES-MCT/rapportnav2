package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionDates
import fr.gouv.dgampa.rapportnav.domain.validation.ValidationPolicies
import java.util.*

@UseCase
class GetComputeNavActionListByMissionId(
    private val processNavAction: ProcessNavAction,
    private val getNavActionListByOwnerId: GetNavActionListByOwnerId,
    private val getMissionDates: GetMissionDates
) {
    /**
     * Fetched by Int missionId: the list may include inquiry actions whose dates come from their own inquiry
     * rather than the mission, so the validation policy is resolved per action (not hoisted here).
     */
    fun execute(missionId: Int, bypassValidation: Boolean = false): List<MissionNavActionEntity> {
        val actions = getNavActionListByOwnerId.execute(missionId = missionId)
        return actions.map { processNavAction.execute(action = it, bypassValidation = bypassValidation) }
    }

    fun execute(ownerId: UUID, bypassValidation: Boolean = false): List<MissionNavActionEntity> {
        val actions = getNavActionListByOwnerId.execute(ownerId = ownerId)

        // All actions fetched by ownerId belong to the same nav mission, so their dates — and thus the
        // validation policy — are identical. Resolve it once instead of per action. Skipped when validation
        // is bypassed (the policy is then unused).
        val policy = if (bypassValidation) null
        else ValidationPolicies.forMissionStartDate(
            getMissionDates.execute(missionId = null, ownerId = ownerId)?.startDateTimeUtc
        )

        return actions.map { processNavAction.execute(action = it, bypassValidation = bypassValidation, policy = policy) }
    }
}
