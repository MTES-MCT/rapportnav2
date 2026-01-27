package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.controlUnit.IEnvControlUnitRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnv
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2

@UseCase
class CreateEnvMission(
    private val monitorEnvRepo: IEnvMissionRepository,
    private val monitorEnvControlUnitRepo: IEnvControlUnitRepository
) {

    fun execute(missionGeneralInfo: MissionGeneralInfo2, controlUnitIds: List<Int>?): MissionEnvEntity {
        val controlUnits = getControlUnits(controlUnitIds = controlUnitIds ?: emptyList())

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
            ?: throw BackendInternalException(
                message = "CreateEnvMission: Failed to create mission in MonitorEnv"
            )
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

        if (controlUnits.isEmpty()) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "CreateEnvMission: controlUnits is empty for this user"
            )
        }
        return controlUnits
    }
}
