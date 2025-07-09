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
            val controlUnits = getControlUnits(controlUnitIds = controlUnitIds?: emptyList())

            val missionEnv = MissionEnv(
                id = missionGeneralInfo.missionId,
                missionTypes = missionGeneralInfo.missionTypes,
                missionSource = MissionSourceEnum.RAPPORT_NAV,
                startDateTimeUtc = missionGeneralInfo.startDateTimeUtc,
                endDateTimeUtc = missionGeneralInfo.endDateTimeUtc,
                controlUnits = controlUnits,
                hasMissionOrder = false, //TODO : a checker
                isUnderJdp = false, //TODO: a checker
                isGeometryComputedFromControls = false, //TODO: a checker
            )
            return monitorEnvRepo.createMission(missionEnv)

        } catch (e: Exception) {
            logger.error("CreateOrUpdateEnvMission failed creating missions", e)
            return null
        }
    }

    fun getControlUnits(controlUnitIds: List<Int>): List<LegacyControlUnitEntity> {
        val controlUnits = monitorEnvControlUnitRepo
            .findAll()
            ?.filter { it.id in controlUnitIds }
            ?.map {
                LegacyControlUnitEntity(
                    id = it.id,
                    name = it.name,
                    contact = it.contact,
                    isArchived = it.isArchived,
                    administration = it.administration
                )
            } ?: emptyList()
        if (controlUnits.isEmpty()) throw Exception("CreateEnvMission : controlUnits is empty for this user")
        return controlUnits
    }
}
