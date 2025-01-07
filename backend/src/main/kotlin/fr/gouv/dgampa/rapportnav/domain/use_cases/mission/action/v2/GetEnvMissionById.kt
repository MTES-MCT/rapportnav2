package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IEnvMissionRepository
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable

@UseCase
class GetEnvMissionById(
    private val monitorEnvApiRepo: IEnvMissionRepository
) {
    private val logger = LoggerFactory.getLogger(GetEnvMissionById::class.java)

    @Cacheable(value = ["envMission"], key = "#missionId")
    fun execute(missionId: Int?): MissionEntity? {
        if (missionId == null) {
            logger.error("GetEnvMissionById received a null missionId")
            throw IllegalArgumentException("GetEnvMissionById should not receive null missionId")
        }
        return try {
            monitorEnvApiRepo.findMissionById(missionId = missionId)
        } catch (e: Exception) {
            logger.error("GetEnvMissionById failed loading mission", e)
            return null
        }
    }
}
