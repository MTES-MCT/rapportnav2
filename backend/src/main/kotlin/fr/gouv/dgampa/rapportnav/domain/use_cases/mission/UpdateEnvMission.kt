package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IEnvMissionRepository
import org.slf4j.LoggerFactory

@UseCase
class UpdateEnvMission(
    private val monitorEnvApiRepo: IEnvMissionRepository,
) {
    private val logger = LoggerFactory.getLogger(UpdateEnvMission::class.java)

    fun execute(
        mission: MissionEntity,
    ): MissionEntity? {
        val savedMission = monitorEnvApiRepo.updateMission(mission)
        return savedMission
    }
}
