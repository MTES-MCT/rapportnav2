package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.CreateEnvMission
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2

@UseCase
class CreateMission(
    private val createEnvMission: CreateEnvMission,
    private val createNavMission: CreateMissionNav,
    private val createGeneralInfos: CreateGeneralInfos

) {
    fun execute(generalInfo2: MissionGeneralInfo2, service: ServiceEntity? = null): MissionEntity2? {
        if (service?.id == null || service.controlUnits.isNullOrEmpty()) {
            throw Exception("Error while saving mission nav. Control Units are empty for this user")
        }

        if (generalInfo2.isMissionNav()) {
            return executeNavMission(
                serviceId = service.id,
                generalInfo2 = generalInfo2
            )
        }

        return executeEnvMission(
            generalInfo2 = generalInfo2,
            controlUnitIds = service.controlUnits
        )
    }

    private fun executeNavMission(generalInfo2: MissionGeneralInfo2, serviceId: Int): MissionEntity2? {
        val missionNav = createNavMission.execute(
            generalInfo2 = generalInfo2,
            serviceId = serviceId
        ) ?: return null
        val generalInfosEntity = createGeneralInfos.execute(missionIdUUID = missionNav.id, generalInfo2 = generalInfo2)

        return MissionEntity2(
            idUUID = missionNav.id,
            generalInfos = generalInfosEntity,
            data = MissionEntity(
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

    private fun executeEnvMission(generalInfo2: MissionGeneralInfo2, controlUnitIds: List<Int>?): MissionEntity2? {
        val missionEnv = createEnvMission.execute(generalInfo2, controlUnitIds) ?: return null
        val generalInfosEntity = createGeneralInfos.execute(missionId = missionEnv.id, generalInfo2 = generalInfo2)

        return MissionEntity2(
            id = missionEnv.id!!,
            actions = listOf(),
            generalInfos = generalInfosEntity,
            data = MissionEntity(
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
