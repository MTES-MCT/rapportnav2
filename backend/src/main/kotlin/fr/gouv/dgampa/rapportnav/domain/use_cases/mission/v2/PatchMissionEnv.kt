package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvMissionById2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.controlUnitResource.GetControlUnitResources
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.MissionEnvInput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.APIEnvMissionRepositoryV2
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Caching

@UseCase
class PatchMissionEnv(
    private val getEnvMissionById2: GetEnvMissionById2,
    private val apiEnvRepo2: APIEnvMissionRepositoryV2,
    private val getControlUnitResources: GetControlUnitResources
) {
    private val logger = LoggerFactory.getLogger(PatchMissionEnv::class.java)

    @Caching(
        evict = [
            CacheEvict(value = ["envMission"], key = "#input.missionId"),
            CacheEvict(value = ["envMission2"], key = "#input.missionId"),
        ]
    )
    fun execute(input: MissionEnvInput): MissionEnvEntity? {
        val fromEnvMission = getEnvMissionById2.execute(missionId = input.missionId) ?: return null

        if (input.equals(
                MissionEnvInput.fromMissionEntity(
                    entity = fromEnvMission,
                    controlUnitId = input.controlUnitId
                )
            )
        ) return null

        logger.info("patchInputMission : $input")
        logger.info("fromEnvMission : $fromEnvMission")

        val enrichedInput = input.copy(resources = enrichResourceTypes(input.resources))

        return apiEnvRepo2.patchMission(
            missionId = input.missionId,
            mission = enrichedInput.toPatchMissionInput(controlUnits = fromEnvMission.controlUnits)
        )
    }

    private fun enrichResourceTypes(
        resources: List<LegacyControlUnitResourceEntity>?
    ): List<LegacyControlUnitResourceEntity>? {
        if (resources.isNullOrEmpty()) return resources

        val resourceIds = resources.map { it.id }.toSet()
        val resourceTypeById = getControlUnitResources.execute()
            .filter { it.id in resourceIds }
            .associate { it.id to it.type }

        return resources.map { resource ->
            if (resource.type == null) resource.copy(type = resourceTypeById[resource.id])
            else resource
        }
    }
}
