package fr.gouv.dgampa.rapportnav.infrastructure.bff

import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.*
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.Mission
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller


@Controller
class MissionController(
    private val getEnvMissions: GetEnvMissions,
    private val getNavMissionById: GetNavMissionById,
    private val getEnvMissionById: GetEnvMissionById,
    private val getFishMissionById: GetFishMissionById,
    private val updateEnvMission: UpdateEnvMission,
) {

    @QueryMapping
    fun missions(@Argument userId: Any): List<Mission> {
        return getEnvMissions.execute(
            userId = null,
            startedAfterDateTime = null,
            startedBeforeDateTime = null,
            pageNumber = null,
            pageSize = null,
        ).map { Mission.fromMissionEntity(it) }
    }

    @QueryMapping
    fun mission(@Argument missionId: Int): Mission {
        val envMission = getEnvMissionById.execute(missionId = missionId)
        val fishMission = getFishMissionById.execute(missionId = missionId)
        val navMission = getNavMissionById.execute(missionId = missionId)

        return Mission.fromMissionEntity(MissionEntity(envMission, navMission, fishMission))
    }

}
