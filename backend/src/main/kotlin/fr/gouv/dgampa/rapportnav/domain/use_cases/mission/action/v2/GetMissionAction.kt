package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity

@UseCase
class GetMissionAction(
    private val getEnvActionByMissionId: GetEnvActionListByMissionId,
    private val getNavActionByMissionId: GetNavActionListByMissionId,
    private val getFIshListActionByMissionId: GetFishActionListByMissionId
) {
    fun execute(missionId: Int?): List<MissionActionEntity> {
        val envActions = getEnvActionByMissionId.execute(missionId = missionId)
        val navActions = getNavActionByMissionId.execute(missionId = missionId)
        val fishActions = getFIshListActionByMissionId.execute(missionId = missionId)
        return (envActions + navActions + fishActions).sortedByDescending { action -> action.startDateTimeUtc }
    }
}
