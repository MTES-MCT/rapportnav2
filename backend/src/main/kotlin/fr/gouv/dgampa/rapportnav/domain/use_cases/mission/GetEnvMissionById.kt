package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IEnvMissionRepository
import org.springframework.cache.annotation.Cacheable

@UseCase
class GetEnvMissionById(
    private val monitorEnvApiRepo: IEnvMissionRepository
) {
    @Cacheable(value = ["envMission2"], key = "#missionId")
    fun execute(missionId: Int): MissionEnvEntity? {
        return monitorEnvApiRepo.findMissionById(missionId = missionId)
    }
}
