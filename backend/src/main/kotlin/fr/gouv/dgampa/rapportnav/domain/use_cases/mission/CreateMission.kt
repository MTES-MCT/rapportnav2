package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.inputs.CreateOrUpdateMissionDataInput
import org.slf4j.LoggerFactory

@UseCase
class CreateMission(
    private val monitorEnvRepo: IEnvMissionRepository
) {
    private val logger = LoggerFactory.getLogger(CreateMission::class.java)


    fun execute(input: CreateOrUpdateMissionDataInput): MissionEntity? {
        try {
            return monitorEnvRepo.createMission(input)
        } catch (e: Exception) {
            logger.error("CreateMission failed creating missions", e)
            return null
        }
    }
}
