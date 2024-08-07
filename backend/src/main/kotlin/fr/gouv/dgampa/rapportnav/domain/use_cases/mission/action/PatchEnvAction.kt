package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.PatchedEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.action.ActionEnvInput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input.PatchActionInput

@UseCase
class PatchEnvAction(private val envRepository: IEnvMissionRepository) {
    fun execute(
        input: ActionEnvInput,
    ): PatchedEnvActionEntity? {
        return envRepository.patchAction(
            input.actionId,
            PatchActionInput(
                observationsByUnit = input.observationsByUnit,
                actionStartDateTimeUtc = input.startDateTimeUtc,
                actionEndDateTimeUtc = input.endDateTimeUtc,
            )
        );
    }
}
