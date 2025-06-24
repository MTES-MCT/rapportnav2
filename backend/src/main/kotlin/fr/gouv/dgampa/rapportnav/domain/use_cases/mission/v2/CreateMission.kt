package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.CreateEnvMission
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2

@UseCase
class CreateMission(
    private val createEnvMission: CreateEnvMission,
    private val createNavMission: CreateMissionNav
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

        return MissionEntity2(
            idUUID = missionNav.id,
            data = MissionEntity(
                idUUID = missionNav.id,
                startDateTimeUtc = missionNav.startDateTimeUtc,
                endDateTimeUtc = missionNav.endDateTimeUtc,
                isUnderJdp = false,
                isDeleted = missionNav.isDeleted,
                isGeometryComputedFromControls = false,
                missionSource = missionNav.missionSource ?: MissionSourceEnum.RAPPORT_NAV,
                hasMissionOrder = false,
            ),
            generalInfos = MissionGeneralInfoEntity2(
                data = MissionGeneralInfoEntity(
                    id = null,
                    missionIdUUID = missionNav.id,
                    missionReportType = generalInfo2.missionReportType
                )
            )
        )
    }

    private fun executeEnvMission(generalInfo2: MissionGeneralInfo2, controlUnitIds: List<Int>?): MissionEntity2? {
        val missionEnv = createEnvMission.execute(generalInfo2, controlUnitIds) ?: return null

        return MissionEntity2(
            id = missionEnv.id!!,
            actions = listOf(),
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
