package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.controlUnit.IEnvControlUnitRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnv
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import org.slf4j.LoggerFactory

@UseCase
class CreateEnvMission(
    private val monitorEnvRepo: IEnvMissionRepository,
    private val monitorEnvControlUnitRepo: IEnvControlUnitRepository
) {
    private val logger = LoggerFactory.getLogger(CreateEnvMission::class.java)

    fun execute(missionGeneralInfo: MissionGeneralInfo2, controlUnitIds: List<Int>?): MissionEnvEntity? {
        try {
            var matchedControlUnits: List<LegacyControlUnitEntity> = listOf()

            if (controlUnitIds !== null && controlUnitIds.isNotEmpty()) {
                val controlUnits = monitorEnvControlUnitRepo.findAll()

                 matchedControlUnits = controlUnits
                    ?.filter { it.id in controlUnitIds }
                    ?: emptyList()


            }
            val missionEnv = MissionEnv(
                missionTypes = missionGeneralInfo.missionTypes,
                missionSource = MissionSourceEnum.RAPPORTNAV,
                startDateTimeUtc = missionGeneralInfo.startDateTimeUtc,
                endDateTimeUtc = missionGeneralInfo.endDateTimeUtc,
                controlUnits = matchedControlUnits,
                hasMissionOrder = false, //TODO : a checker
                isUnderJdp = false, //TODO: a checker
                isGeometryComputedFromControls = false, //TODO: a checker
            )

            return monitorEnvRepo.createMission(missionEnv)


            throw Exception("CreateEnvMission : controlUnit is empty for this user")

        } catch (e: Exception) {
            logger.error("CreateEnvMission failed creating missions", e)
            return null
        }
    }
}
