package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IEnvMissionRepository
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable

@UseCase
class GetEnvMissionById2(
    private val monitorEnvApiRepo: IEnvMissionRepository
) {
    private val logger = LoggerFactory.getLogger(GetEnvMissionById2::class.java)

    @Cacheable(value = ["envMission2"], key = "#missionId")
    fun execute(missionId: Int): MissionEntity? {
        return try {
            monitorEnvApiRepo.findMissionById(missionId = missionId)
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            logger.error("GetEnvMissionById failed loading mission id=$missionId", e)
            throw BackendInternalException(
                message = "Failed to fetch env mission $missionId from MonitorEnv",
                originalException = e
            )
        }
    }
}
