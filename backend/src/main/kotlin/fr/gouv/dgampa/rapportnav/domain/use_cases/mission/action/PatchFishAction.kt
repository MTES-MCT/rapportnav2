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

    @Caching(
        evict = [
            CacheEvict(value = ["fishActions"], key = "#input.missionId"),
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
            )
        )
        return patchedAction
    }
}
