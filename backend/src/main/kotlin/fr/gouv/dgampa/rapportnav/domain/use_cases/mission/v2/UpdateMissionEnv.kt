package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvMissionById2
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.MissionEnvInput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.APIEnvMissionRepositoryV2
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Caching

@UseCase
class UpdateMissionEnv(
    private val getEnvMissionById2: GetEnvMissionById2,
    private val apiEnvRepo2: APIEnvMissionRepositoryV2

) {

    private val logger = LoggerFactory.getLogger(UpdateMissionEnv::class.java)

    @Caching(
        evict = [
            CacheEvict(value = ["envMission"], key = "#input.missionId"),
            CacheEvict(value = ["envMission2"], key = "#input.missionId"),
        ]
    )
    fun execute(input: MissionEnvInput): MissionEnvEntity? {
        val fromDbEnvMission = getEnvMissionById2.execute(input.missionId) ?: return null

        val fromDbEnvInput = MissionEnvInput.fromMissionEntity(
            missionEntity = fromDbEnvMission,
            controlUnitId = input.resources?.firstOrNull()?.controlUnitId
        )

        logger.info("fromDbEnvInput : $fromDbEnvInput")

        logger.info("input : $input")

        if (input.equals(fromDbEnvInput)) return null

        return try {
            apiEnvRepo2.update(input.toMissionEnvEntity(fromDbEnvMission, controlUnitId = input.resources?.firstOrNull()?.controlUnitId))
        } catch (e: Exception) {
            logger.error("Update Mission failed", e)
            return null
        }
    }
}
