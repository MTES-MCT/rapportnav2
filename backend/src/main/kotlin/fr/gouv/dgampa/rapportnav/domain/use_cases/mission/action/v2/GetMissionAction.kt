package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity

@UseCase
class GetMissionAction(
    private val getEnvActionByMissionId: GetComputeEnvActionListByMissionId,
    private val getNavActionByMissionId: GetComputeNavActionListByMissionId,
    private val getFIshListActionByMissionId: GetComputeFishActionListByMissionId
) {
    fun execute(missionId: String?): List<MissionActionEntity> {
        val envActions = getEnvActionByMissionId.execute(missionId = missionId)
        val navActions = getNavActionByMissionId.execute(missionId = missionId)
        val fishActions = getFIshListActionByMissionId.execute(missionId = missionId)
        return (envActions + navActions + fishActions).sortedByDescending { action -> action.startDateTimeUtc }
    }
}
