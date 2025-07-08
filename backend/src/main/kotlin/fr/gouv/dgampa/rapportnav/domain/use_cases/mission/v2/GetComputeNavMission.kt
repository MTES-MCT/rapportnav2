package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeNavActionListByMissionId
import java.util.*

@UseCase
class GetComputeNavMission(
    private val getGeneralInfo2: GetGeneralInfo2,
    private val getNavMissionById2: GetNavMissionById2,
    private val getComputeNavActionListByMissionId: GetComputeNavActionListByMissionId
) {
    fun execute(missionId: UUID? = null, navMission: MissionNavEntity? = null): MissionEntity2? {
        val mission = navMission ?: missionId?.let { getNavMissionById2.execute(it) } ?: return null

        val generalInfos = getGeneralInfo2.execute(missionIdUUID = mission.id)
        val actions = getComputeNavActionListByMissionId.execute(ownerId = mission.id)

        return MissionEntity2(
            idUUID = mission.id,
            actions = actions,
            generalInfos = generalInfos,
            data = MissionEntity.fromMissionNavEntity(entity = mission)
        )
    }
}
