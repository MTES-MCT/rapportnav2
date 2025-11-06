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
class PatchMissionEnv(
    private val getEnvMissionById2: GetEnvMissionById2,
    private val apiEnvRepo2: APIEnvMissionRepositoryV2
) {
    private val logger = LoggerFactory.getLogger(PatchMissionEnv::class.java)

    @Caching(
        evict = [
            CacheEvict(value = ["envMission"], key = "#input.missionId"),
            CacheEvict(value = ["envMission2"], key = "#input.missionId"),
        ]
    )
    fun execute(input: MissionEnvInput): MissionEnvEntity? {
        val controlUnitId = input.resources?.firstOrNull()?.controlUnitId
        val fromEnvMission = getEnvMissionById2.execute(missionId = input.missionId) ?: return null

        if (input.equals(
                MissionEnvInput.fromMissionEntity(
                    entity = fromEnvMission,
                    controlUnitId = controlUnitId
                )
            )
        ) return null

        logger.info("patchInputMission : $input")
        logger.info("fromEnvMission : $fromEnvMission")


        return try {
            apiEnvRepo2.patchMission(
                missionId = input.missionId,
                mission = input.toPatchMissionInput(controlUnits = fromEnvMission.controlUnits)
            )
        } catch (e: Exception) {
            logger.error("Update Mission failed", e)
            return null
        }
    }
}
