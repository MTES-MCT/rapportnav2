package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvMissionById2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetMissionAction

@UseCase
class GetMission2(
    private val getGeneralInfos2: GetGeneralInfo2,
    private val getMissionAction: GetMissionAction,
    private val getEnvMissionById2: GetEnvMissionById2,
    private val getNavMissionById2: GetNavMissionById2
) {
    fun execute(missionId: String? = null, envMission: MissionEntity? = null): MissionEntity2? {
        val mission = envMission ?: missionId?.let { getEnvMissionById2.execute(it) ?: getNavMissionById2.execute(it) } ?: return null

        val id = if (mission.missionIdString !== null) mission.missionIdString else if (mission.id !== null) mission.id.toString() else return null

        val actions = getMissionAction.execute(missionId = id.toInt())
        val generalInfos = getGeneralInfos2.execute(missionId = id, controlUnits = mission.controlUnits)

        return MissionEntity2(
            id = id.toIntOrNull() ?: 0, // TODO : TO REMOVE AS SOON AS POSSIBLE (STEP3 POSSIBLE)
            data = mission,
            actions = actions,
            generalInfos = generalInfos
        )
    }
}
