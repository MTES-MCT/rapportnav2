package fr.gouv.dgampa.rapportnav.infrastructure.bff

import fr.gouv.dgampa.rapportnav.domain.entities.mission.Mission
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum.CONTROL
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum.SURVEILLANCE
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.*
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.status.GetLastStartedStatusForMission
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
    private val getLastStartedStatusForMission: GetLastStartedStatusForMission,
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

    @SchemaMapping(typeName = "Action", field = "status")
    fun getActionStatus(action: Action2): ActionStatusType {
        // get time for this action
        val time = action.startDateTimeUtc

        // get last started status for this time and missionId
        val lastStartedStatus = getLastStartedStatusForMission.exexute(missionId=action.missionId, actionStartDateTimeUtc=action.startDateTimeUtc)

        return lastStartedStatus
    }

    @SchemaMapping(typeName = "Action", field = "type")
    fun getActionType(action: Action2?): ActionType {
        if (action?.data != null) {
            return when (action.data) {
                is EnvActionData -> {
                    return when (action.data.actionType) {
                        CONTROL -> ActionType.CONTROL
                        SURVEILLANCE -> ActionType.SURVEILLANCE
                        else -> ActionType.NOTE
                    }
                }
                is FishActionData -> {
                    return if (action.data.actionType in setOf(
                            MissionActionType.SEA_CONTROL,
                            MissionActionType.AIR_CONTROL,
                            MissionActionType.LAND_CONTROL
                        )) {
                        ActionType.CONTROL
                    } else {
                        ActionType.SURVEILLANCE
                    }
                }
                is NavActionData -> action.data.actionType
                else -> ActionType.OTHER

            }
        }
        return ActionType.OTHER
    }

    @SchemaMapping(typeName = "Mission", field = "actions")
    fun combineActions(mission: Mission?): List<Action2>? {
        if (mission?.actions != null) {
            val allActions: List<Action2> = mission.actions.map { missionAction ->
                when (missionAction) {
                    is MissionAction.EnvAction -> Action2.fromEnvAction(missionAction.envAction as EnvActionEntity, missionId = mission.id)
                    is MissionAction.FishAction -> Action2.fromFishAction(missionAction.fishAction as fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction, missionId = mission.id)
                    is MissionAction.NavAction -> Action2.fromNavAction(missionAction.navAction as NavAction)
                }
            }
            val sortedActions = Action2.sortForTimeline(allActions)
            return sortedActions
        }
        return null
    }

}
