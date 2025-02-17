package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.MissionEnvInput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input.PatchMissionInput
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Caching

@UseCase
class PatchEnvMission(private val envRepository: IEnvMissionRepository) {

    @Caching(
        evict = [
            CacheEvict(value = ["envMission"], key = "#input.missionId"),
            CacheEvict(value = ["envMission2"], key = "#input.missionId"),
        ]
    )
    fun execute(
        input: MissionEnvInput,
    ): MissionEntity? {
        //TODO: Compare before update
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
