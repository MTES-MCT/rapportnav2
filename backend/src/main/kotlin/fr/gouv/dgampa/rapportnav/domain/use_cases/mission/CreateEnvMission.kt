package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.controlUnit.IEnvControlUnitRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.inputs.CreateOrUpdateMissionDataInput
import org.slf4j.LoggerFactory

@UseCase
class CreateEnvMission(
    private val monitorEnvRepo: IEnvMissionRepository,
    private val monitorEnvControlUnitRepo: IEnvControlUnitRepository
) {
    private val logger = LoggerFactory.getLogger(CreateEnvMission::class.java)


    fun execute(input: CreateOrUpdateMissionDataInput, controlUnitIds: List<Int>?): MissionEntity? {
        try {
            if (controlUnitIds !== null && controlUnitIds.isNotEmpty()) {
                val controlUnits = monitorEnvControlUnitRepo.findAll()

                val matchedControlUnits: List<LegacyControlUnitEntity> = controlUnits
                    ?.filter { it.id in controlUnitIds }
                    ?: emptyList()

                input.controlUnits = matchedControlUnits

                return monitorEnvRepo.createMission(input)
            }

            throw Exception("CreateEnvMission : controlUnit is empty for this user")

        } catch (e: Exception) {
            logger.error("CreateEnvMission failed creating missions", e)
            return null
        }
    }
}
