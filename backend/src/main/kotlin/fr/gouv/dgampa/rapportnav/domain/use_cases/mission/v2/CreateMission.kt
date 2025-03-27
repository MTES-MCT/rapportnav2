package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReportTypeEnum
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.CreateEnvMission
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2

@UseCase
class CreateMission(
    private val createEnvMission: CreateEnvMission,
    private val createNavMission: CreateMissionNav
) {

    fun execute(generalInfo2: MissionGeneralInfo2, controlUnitIds: List<Int>? = null): MissionEntity2? {

        if (controlUnitIds === null || controlUnitIds.isEmpty()) {
            throw Exception("Error while saving mission nav. Control Units are empty for this user")
        }

        if (generalInfo2.missionReportType === MissionReportTypeEnum.OFFICE_REPORT || generalInfo2.missionReportType === MissionReportTypeEnum.EXTERNAL_REINFORCEMENT_TIME_REPORT) {

            val missionNav = createNavMission.execute(
                generalInfo2 = generalInfo2,
                controlUnitIds = controlUnitIds
            )

            return MissionEntity2(
                id = missionNav?.id!!,
                actions = listOf(),
                envData = MissionEntity(
                    id = missionNav.id,
                    missionSource = MissionSourceEnum.RAPPORT_NAV,
                    startDateTimeUtc = missionNav.startDateTimeUtc,
                    isDeleted = missionNav.isDeleted,
                    endDateTimeUtc = missionNav.endDateTimeUtc,
                    missionTypes = missionNav.missionTypes,
                    controlUnits = listOf(),
                    hasMissionOrder = false,
                    isGeometryComputedFromControls = false,
                    isUnderJdp = false
                )
            )
        }

        val missionEnv = createEnvMission.execute(generalInfo2, controlUnitIds) ?: return null

        return MissionEntity2(
            id = missionEnv.id!!,
            actions = listOf(),
            envData = MissionEntity(
                id = missionEnv.id,
                missionTypes = missionEnv.missionTypes,
                startDateTimeUtc = missionEnv.startDateTimeUtc,
                endDateTimeUtc = missionEnv.endDateTimeUtc,
                isUnderJdp = missionEnv.isUnderJdp!!,
                isGeometryComputedFromControls = missionEnv.isGeometryComputedFromControls!!,
                hasMissionOrder = missionEnv.hasMissionOrder!!,
                missionSource = missionEnv.missionSource!!,
                controlUnits = missionEnv.controlUnits!!,
                isDeleted = missionEnv.isDeleted!!,
            )
        )
    }
}
