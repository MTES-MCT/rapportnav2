package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.MissionEnvInput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input.PatchMissionInput

@UseCase
class PatchEnvMission(private val envRepository: IEnvMissionRepository) {
    fun execute(
        input: MissionEnvInput,
    ): MissionEntity? {
        return envRepository.patchMission(
            input.missionId,
            PatchMissionInput(
                observationsByUnit = input.observationsByUnit,
                startDateTimeUtc = input.startDateTimeUtc,
                endDateTimeUtc = input.endDateTimeUtc,
            )
        );
    }
}
