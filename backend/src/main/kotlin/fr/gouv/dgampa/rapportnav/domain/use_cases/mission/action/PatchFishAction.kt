package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IFishActionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.action.ActionFishInput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorfish.input.PatchActionInput
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Caching

@UseCase
class PatchFishAction(private val fishRepository: IFishActionRepository) {

    // These caches are keyed by the MonitorFish external id (Int), so evict by input.externalId
    // (resolved from the mission UUID upstream), not by the UUID itself. The condition guards
    // against a null external id producing a bogus eviction key.
    // Note: only "fishActionList" currently has a @Cacheable reader; "fishActions" is kept in
    // sync for when a reader is added.
    @Caching(
        evict = [
            CacheEvict(value = ["fishActions"], key = "#input.externalId", condition = "#input.externalId != null"),
            CacheEvict(value = ["fishActionList"], key = "#input.externalId", condition = "#input.externalId != null"),
        ]
    )
    fun execute(
        input: ActionFishInput,
    ): MissionAction? {
        val patchedAction = fishRepository.patchAction(
            input.actionId,
            PatchActionInput(
                observationsByUnit = input.observationsByUnit,
                actionDatetimeUtc = input.startDateTimeUtc,
                actionEndDatetimeUtc = input.endDateTimeUtc,
                incidentDuringOperation = input.incidentDuringOperation,
                hasDivingDuringOperation = input.hasDivingDuringOperation
            )
        )
        return patchedAction
    }
}
