package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvMissionById2
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input.PatchMissionInput
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
            CacheEvict(value = ["envMission"], key = "#missionId"),
            CacheEvict(value = ["envMission2"], key = "#missionId"),
        ]
    )
    fun execute(missionId: Int, input: PatchMissionInput, resources: List<LegacyControlUnitResourceEntity>?): MissionEnvEntity? {
        val fromEnvMission = getEnvMissionById2.execute(missionId) ?: return null
        val mission = input.withControlUnits(resources = resources, controlUnits = fromEnvMission.controlUnits)

        logger.info("patchInputMission : $mission")
        logger.info("fromEnvMission : $fromEnvMission")
        if(mission.hasNotChanged(fromEnvMission)) return null

        return try {
            apiEnvRepo2.patchMission(missionId = missionId, mission = mission)
        } catch (e: Exception) {
            logger.error("Update Mission failed", e)
            return null
        }
    }
}
