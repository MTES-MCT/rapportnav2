package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.PatchedEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.action.ActionEnvInput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input.PatchActionInput
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Caching

@UseCase
class PatchEnvAction(private val envRepository: IEnvMissionRepository) {

    // These caches are keyed by the MonitorEnv external id (Int), so evict by input.externalId
    // (resolved from the mission UUID upstream), not by the UUID itself. The condition guards
    // against a null external id producing a bogus eviction key.
    // Note: only "envMission2" currently has a @Cacheable reader; the others are kept in sync
    // for when readers are added.
    @Caching(
        evict = [
            CacheEvict(value = ["envMission"], key = "#input.externalId", condition = "#input.externalId != null"),
            CacheEvict(value = ["envMission2"], key = "#input.externalId", condition = "#input.externalId != null"),
            CacheEvict(value = ["envActionList"], key = "#input.externalId", condition = "#input.externalId != null"),
        ]
    )
    fun execute(
        input: ActionEnvInput,
    ): PatchedEnvActionEntity? {
        return envRepository.patchAction(
            input.actionId,
            PatchActionInput(
                observationsByUnit = input.observationsByUnit,
                actionStartDateTimeUtc = input.startDateTimeUtc,
                actionEndDateTimeUtc = input.endDateTimeUtc,
                incidentDuringOperation = input.incidentDuringOperation,
                hasDivingDuringOperation = input.hasDivingDuringOperation,
            )
        );
    }
}
