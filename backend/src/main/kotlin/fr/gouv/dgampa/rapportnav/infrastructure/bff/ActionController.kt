package fr.gouv.dgampa.rapportnav.infrastructure.bff

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum.CONTROL
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum.SURVEILLANCE
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatus
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.status.AddStatus
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.status.GetLastStartedStatusForMission
import fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.ActionStatusInput
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.*
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller


@Controller
class ActionController(
    private val getLastStartedStatusForMission: GetLastStartedStatusForMission,
    private val addStatus: AddStatus,
) {

    @MutationMapping
    fun addOrUpdateStatus(@Argument statusAction: ActionStatusInput): ActionStatus {
        val data = statusAction.toActionStatus()
        val savedData = addStatus.execute(data)
        return savedData
    }

    @SchemaMapping(typeName = "Action", field = "status")
    fun getActionStatus(action: Action2): ActionStatusType {

        // if already a status action - no need to recompute
        if (action.source == MissionSourceEnum.RAPPORTNAV) {
            if (action.data is NavActionStatus) {
                return action.data.status
            }
        }

        // get last started status for this time and missionId
        val lastStartedStatus = getLastStartedStatusForMission.execute(missionId=action.missionId, actionStartDateTimeUtc=action.startDateTimeUtc)

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
                is NavActionControl -> ActionType.CONTROL
                is NavActionStatus -> ActionType.STATUS
                else -> ActionType.OTHER

            }
        }
        return ActionType.OTHER
    }

}
