package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity2
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
                id = missionNav.id.toString(),
                data = MissionEntity(
                    id = missionNav.id.toString(),
                    startDateTimeUtc = missionNav.startDateTimeUtc,
                    endDateTimeUtc = missionNav.endDateTimeUtc,
                    isUnderJdp = false,
                    isDeleted = false,
                    isGeometryComputedFromControls = false,
                    missionSource = MissionSourceEnum.RAPPORT_NAV,
                    hasMissionOrder = false
                )
            )
        }

        val missionEnv = createEnvMission.execute(generalInfo2, controlUnitIds) ?: return null

        return MissionEntity2(
            id = missionEnv.id.toString(),
            actions = listOf(),
            data = MissionEntity(
                id = missionEnv.id.toString(),
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
