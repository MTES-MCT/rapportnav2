package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
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

    fun execute(generalInfo2: MissionGeneralInfo2, controlUnitIds: List<Int>? = null): MissionEntity2? {

        if (controlUnitIds.isNullOrEmpty()) {
            throw Exception("Error while saving mission nav. Control Units are empty for this user")
        }

        if (generalInfo2.isMissionNav()) {
            val missionNav = createNavMission.execute(
                generalInfo2 = generalInfo2,
                controlUnitIds = controlUnitIds
            )?: return null
            return MissionEntity2(
                id = missionNav.id!!,
                data = MissionEntity(
                    id = missionNav.id,
                    startDateTimeUtc = missionNav.startDateTimeUtc,
                    endDateTimeUtc = missionNav.endDateTimeUtc,
                    isUnderJdp = false,
                    isDeleted = missionNav.isDeleted,
                    isGeometryComputedFromControls = false,
                    missionSource = missionNav.missionSource ?: MissionSourceEnum.RAPPORT_NAV,
                    hasMissionOrder = false,
                    missionIdString = missionNav.missionIdString
                ),
                generalInfos = MissionGeneralInfoEntity2(
                   data = MissionGeneralInfoEntity(
                       id = null,
                       missionIdString = missionNav.missionIdString,
                       missionReportType = generalInfo2.missionReportType
                   )
                )
            )
        }

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
                isDeleted = missionEnv.isDeleted!!,
                missionIdString = missionEnv.id.toString()
            )
        )
    }
}
