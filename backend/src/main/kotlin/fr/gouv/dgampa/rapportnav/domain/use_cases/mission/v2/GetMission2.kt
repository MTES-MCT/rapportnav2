package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetMissionAction

@UseCase
class GetMission2(
    private val getGeneralInfos2: GetGeneralInfo2,
    private val getMissionAction: GetMissionAction,
) {
    fun execute(mission: MissionEntity): MissionEntity2? {
        if(mission.id == null) return null
        val actions = getMissionAction.execute(missionId = mission.id)
        val generalInfos = getGeneralInfos2.execute(missionId = mission.id, controlUnits = mission.controlUnits)

        return MissionEntity2(
            id = mission.id,
            envData = mission,
            actions = actions,
            generalInfos = generalInfos
        )
    }
}
