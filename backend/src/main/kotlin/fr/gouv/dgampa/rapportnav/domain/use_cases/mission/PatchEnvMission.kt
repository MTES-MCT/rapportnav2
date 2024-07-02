package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.PatchMissionInput
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.MissionEnvInput
import org.slf4j.LoggerFactory

@UseCase
class PatchEnvMission( private val envRepository: IEnvMissionRepository) {
    private val logger = LoggerFactory.getLogger(UpdateEnvMission::class.java);
    fun execute(
        input: MissionEnvInput,
    ): MissionEntity? {
        return envRepository.patchMission(
            input.missionId,
            PatchMissionInput(observationsByUnit = input.observationsByUnit)
        );
    }
}
