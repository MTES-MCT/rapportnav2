package fr.gouv.dgampa.rapportnav.infrastructure.bff

import fr.gouv.dgampa.rapportnav.domain.entities.mission.Mission
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.*
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
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
        val missions = getEnvMissions.execute(
            userId = null,
            startedAfterDateTime = null,
            startedBeforeDateTime = null,
            pageNumber = null,
            pageSize = null,
        )
        return missions
    }

    @QueryMapping
    fun missionById(@Argument missionId: Int): Mission {
        val envMission = getEnvMissionById.execute(missionId = missionId)
        val fishMission = getFishMissionById.execute(missionId = missionId)
        val navMission = getNavMissionById.execute(missionId = missionId)

        val mission = Mission(envMission, navMission, fishMission)
        return mission
    }

    @SchemaMapping(typeName = "NavAction", field = "actionStatus")
    fun getStatus(action: NavAction?): ActionStatusType {
        // get time for this action

        // get last started status for this time and missionId

        return ActionStatusType.DOCKING
    }

}
