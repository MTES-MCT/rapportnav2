package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.controlUnit.IEnvControlUnitRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.CreateOrUpdateGeneralInfo
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnv
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import org.slf4j.LoggerFactory

@UseCase
class CreateOrUpdateEnvMission(
    private val monitorEnvRepo: IEnvMissionRepository,
    private val monitorEnvControlUnitRepo: IEnvControlUnitRepository,
    private val createOrUpdateGeneralInfo: CreateOrUpdateGeneralInfo
) {
    private val logger = LoggerFactory.getLogger(CreateOrUpdateEnvMission::class.java)

    fun execute(missionGeneralInfo: MissionGeneralInfo2, controlUnitIds: List<Int>?): MissionEnvEntity? {
        try {
            var matchedControlUnits: List<LegacyControlUnitEntity> = listOf()

            if (controlUnitIds !== null && controlUnitIds.isNotEmpty()) {
                val controlUnits = monitorEnvControlUnitRepo.findAll()

                 matchedControlUnits = controlUnits
                    ?.filter { it.id in controlUnitIds }
                    ?: emptyList()
            }

            if (matchedControlUnits.isEmpty()) {
                throw Exception("CreateOrUpdateEnvMission : controlUnits is empty for this user")
            }

            val missionEnv = MissionEnv(
                id = missionGeneralInfo.missionId,
                missionTypes = missionGeneralInfo.missionTypes,
                missionSource = MissionSourceEnum.RAPPORT_NAV,
                startDateTimeUtc = missionGeneralInfo.startDateTimeUtc,
                endDateTimeUtc = missionGeneralInfo.endDateTimeUtc,
                controlUnits = matchedControlUnits,
                hasMissionOrder = false, //TODO : a checker
                isUnderJdp = false, //TODO: a checker
                isGeometryComputedFromControls = false, //TODO: a checker
            )

            val result = monitorEnvRepo.createMission(missionEnv)

            if (result !== null) {
               createOrUpdateGeneralInfo.execute(
                    MissionGeneralInfo2(
                        id = missionGeneralInfo.id,
                        missionId = result.id!!,
                        startDateTimeUtc = missionGeneralInfo.startDateTimeUtc,
                        endDateTimeUtc = missionGeneralInfo.endDateTimeUtc,
                        missionTypes = missionGeneralInfo.missionTypes,
                        isAllAgentsParticipating = missionGeneralInfo.isAllAgentsParticipating,
                        isWithInterMinisterialService = missionGeneralInfo.isWithInterMinisterialService,
                        missionReportType = missionGeneralInfo.missionReportType,
                        reinforcementType = missionGeneralInfo.reinforcementType,
                        nbHourAtSea = missionGeneralInfo.nbHourAtSea
                    )
                )

                logger.info("GeneralInfo save or update successfully")

            }

            return result

        } catch (e: Exception) {
            logger.error("CreateOrUpdateEnvMission failed creating missions", e)
            return null
        }
    }
}
