package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity
import org.slf4j.LoggerFactory

@UseCase
class GetMissionById(
    private val getEnvMissionById: GetEnvMissionById,
    private val getFishActionsByMissionId: GetFishActionsByMissionId,
    private val getNavMissionById: GetNavMissionById,
) {
    private val logger = LoggerFactory.getLogger(GetMissionById::class.java)

    fun execute(missionId: Int): MissionEntity? {
        val envMission = getEnvMissionById.execute(missionId = missionId) ?: return null
        val fishMissionActions = getFishActionsByMissionId.execute(missionId = missionId)
        val navMission = getNavMissionById.execute(missionId = missionId)

        return MissionEntity(envMission, navMission, fishMissionActions)
    }

}
