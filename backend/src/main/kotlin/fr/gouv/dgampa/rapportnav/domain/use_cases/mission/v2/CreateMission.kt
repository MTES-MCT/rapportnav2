package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.CreateEnvMission
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2

@UseCase
class CreateMission(
    private val createEnvMission: CreateEnvMission,
    private val createNavMission: CreateMissionNav,
    private val createGeneralInfos: CreateGeneralInfos

) {
    fun execute(generalInfo2: MissionGeneralInfo2, service: ServiceEntity? = null): MissionEntity {
        if (service?.id == null || service.controlUnits.isNullOrEmpty()) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "CreateMission: Control Units or Service are falsy"
            )
        }

        if (generalInfo2.isMissionNav()) {
            return executeNavMission(
                service = service,
                generalInfo2 = generalInfo2
            )
        }

        return executeEnvMission(
            generalInfo2 = generalInfo2,
            controlUnitIds = service.controlUnits
        )
    }

    private fun executeNavMission(generalInfo2: MissionGeneralInfo2, service: ServiceEntity): MissionEntity {
        val missionNav = createNavMission.execute(
            generalInfo2 = generalInfo2,
            serviceId = service.id!!
        )
        val generalInfosEntity = createGeneralInfos.execute(missionIdUUID = missionNav.id, generalInfo2 = generalInfo2, service = service)

        return MissionEntity(
            idUUID = missionNav.id,
            generalInfos = generalInfosEntity,
            data = MissionEnvEntity(
                idUUID = missionNav.id,
                startDateTimeUtc = missionNav.startDateTimeUtc,
                endDateTimeUtc = missionNav.endDateTimeUtc,
                isUnderJdp = false,
                isDeleted = missionNav.isDeleted,
                isGeometryComputedFromControls = false,
                missionSource = missionNav.missionSource ?: MissionSourceEnum.RAPPORT_NAV,
                hasMissionOrder = false,

            )
        )
    }

    private fun executeEnvMission(generalInfo2: MissionGeneralInfo2, controlUnitIds: List<Int>?): MissionEntity {
        val missionEnv = createEnvMission.execute(generalInfo2, controlUnitIds)
        val generalInfosEntity = createGeneralInfos.execute(missionId = missionEnv.id, generalInfo2 = generalInfo2)

        return MissionEntity(
            id = missionEnv.id!!,
            actions = listOf(),
            generalInfos = generalInfosEntity,
            data = MissionEnvEntity(
                id = missionEnv.id,
                missionTypes = missionEnv.missionTypes,
                startDateTimeUtc = missionEnv.startDateTimeUtc,
                endDateTimeUtc = missionEnv.endDateTimeUtc,
                isUnderJdp = missionEnv.isUnderJdp!!,
                isGeometryComputedFromControls = missionEnv.isGeometryComputedFromControls!!,
                hasMissionOrder = missionEnv.hasMissionOrder!!,
                missionSource = missionEnv.missionSource!!,
                controlUnits = missionEnv.controlUnits!!,
                isDeleted = missionEnv.isDeleted!!
            )
        )
    }
}
