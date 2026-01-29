package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.PatchedEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.action.ActionEnvInput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input.PatchActionInput
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Caching

@UseCase
class PatchEnvAction(private val envRepository: IEnvMissionRepository) {

    @Caching(
        evict = [
            CacheEvict(value = ["envMission"], key = "#input.missionId"),
            CacheEvict(value = ["envMission2"], key = "#input.missionId"),
            CacheEvict(value = ["envActionList"], key = "#input.missionId"),
        ]
    )
    fun execute(input: ActionEnvInput): PatchedEnvActionEntity? {
        return try {
            envRepository.patchAction(
                input.actionId,
                PatchActionInput(
                    observationsByUnit = input.observationsByUnit,
                    actionStartDateTimeUtc = input.startDateTimeUtc,
                    actionEndDateTimeUtc = input.endDateTimeUtc,
                )
            )
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "PatchEnvAction failed for actionId=${input.actionId}",
                originalException = e
            )
        }
    }
}
