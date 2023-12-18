package fr.gouv.dgampa.rapportnav.infrastructure.bff

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum.CONTROL
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum.SURVEILLANCE
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetEnvActionByIdAndMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetFishActionByIdAndMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.*
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.GetInfractionsByActionId
import fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.action.ActionControlInput
import fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.action.ActionStatusInput
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.action.*
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.infraction.Infraction
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller
import java.util.*


@Controller
class ActionController(
    private val getStatusForAction: GetStatusForAction,
    private val addOrUpdateActionStatus: AddOrUpdateActionStatus,
    private val deleteActionStatus: DeleteActionStatus,
    private val addOrUpdateActionControl: AddOrUpdateActionControl,
    private val deleteActionControl: DeleteActionControl,
    private val getFishActionByIdAndMissionId: GetFishActionByIdAndMissionId,
    private val getEnvActionByIdAndMissionId: GetEnvActionByIdAndMissionId,
    private val getNavActionByIdAndMissionId: GetNavActionByIdAndMissionId,
    private val getInfractionsByActionId: GetInfractionsByActionId
) {

    private val logger = LoggerFactory.getLogger(ActionController::class.java)


    @QueryMapping
    fun actionById(
        @Argument id: String,
        @Argument missionId: Int,
        @Argument source: MissionSourceEnum,
        @Argument type: ActionType
    ): Action? {
        try {
            return when (source) {
                MissionSourceEnum.MONITORFISH -> {
                    val action = getFishActionByIdAndMissionId.execute(
                        id = id.toInt(),
                        missionId = missionId
                    )
                    return action?.let {
                        Action.fromFishAction(
                            fishAction = action,
                            missionId = missionId
                        )
                    }
                }

                MissionSourceEnum.MONITORENV -> {
                    val action = getEnvActionByIdAndMissionId.execute(
                        id = UUID.fromString(id),
                        missionId = missionId
                    )
                    return action?.let {
                        Action.fromEnvAction(
                            envAction = action,
                            missionId = missionId
                        )
                    }
                }

                MissionSourceEnum.RAPPORTNAV -> {
                    val action = getNavActionByIdAndMissionId.execute(
                        id = UUID.fromString(id),
                        missionId = missionId,
                        actionType = type
                    )
                    return action?.let {
                        Action.fromNavAction(
                            navAction = action,
                        )
                    }
                }

                else -> null
            }
        } catch (e: Exception) {
            logger.error("[ERROR] API on endpoint actionById:", e)
            return null
        }
    }

    @MutationMapping
    fun addOrUpdateStatus(@Argument statusAction: ActionStatusInput): NavActionStatus {
        val data = statusAction.toActionStatus()
        return addOrUpdateActionStatus.execute(data).toNavActionStatus()
    }

    @MutationMapping
    fun deleteStatus(@Argument id: UUID): Boolean {
        return deleteActionStatus.execute(id)
    }

    @MutationMapping
    fun addOrUpdateControl(@Argument controlAction: ActionControlInput): NavActionControl {
        val data = controlAction.toActionControl()
        return addOrUpdateActionControl.execute(data).toNavActionControl()
    }

    @MutationMapping
    fun deleteControl(@Argument id: UUID): Boolean {
        val savedData = deleteActionControl.execute(id)
        return savedData
    }

    @SchemaMapping(typeName = "Action", field = "status")
    fun getActionStatus(action: Action): ActionStatusType {

        // if already a status action - no need to recompute
        if (action.source == MissionSourceEnum.RAPPORTNAV) {
            if (action.data is NavActionStatus) {
                return action.data.status
            }
        }

        // get last started status for this time and missionId
        val lastStartedStatus =
            getStatusForAction.execute(missionId = action.missionId, actionStartDateTimeUtc = action.startDateTimeUtc)

        return lastStartedStatus
    }

    @SchemaMapping(typeName = "Action", field = "type")
    fun getActionType(action: Action?): ActionType {
        // TODO move logic to a use case
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
                        )
                    ) {
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

    @SchemaMapping(typeName = "Action", field = "summaryTags")
    fun getActionSummaryTags(action: Action): List<String>? {
        if (action.data is NavActionControl || action.data is FishActionData || action.data is EnvActionData) {
            val navInfractions = getInfractionsByActionId.execute(actionId = action.id.toString())
                .map { Infraction.fromInfractionEntity(it) }

            val navInfractionTypes: List<InfractionTypeEnum> = navInfractions.map {
                it.infractionType
            }.orEmpty().filterNotNull()

            val allInfractionTypes = when (action.data) {
                is NavActionControl -> {
                    navInfractionTypes
                }

                is EnvActionData -> {
                    val envTags: List<InfractionTypeEnum> = action.data.infractions?.map {
                        it.infractionType
                    }.orEmpty()
                    navInfractionTypes + envTags
                }

                is FishActionData -> {
                    val fishTags: List<InfractionType> = listOf(
                        action.data.gearInfractions.map { it.infractionType },
                        action.data.logbookInfractions.map { it.infractionType },
                        action.data.speciesInfractions.map { it.infractionType },
                        action.data.otherInfractions.map { it.infractionType }
                    ).flatten().filterNotNull()

                    navInfractionTypes + fishTags
                }

                else -> {
                    null
                }
            }
            val withReportCount =
                allInfractionTypes?.count { it == InfractionTypeEnum.WITH_REPORT || it == InfractionType.WITH_RECORD }

            val infractionTypeTag = if (withReportCount == 0) {
                "Sans PV"
            } else {
                "$withReportCount PV"
            }

            // NATINfs
            val navNatinfs: List<String> = navInfractions.flatMap {
                it.natinfs ?: emptyList()
            }

            val allNatinfs = when (action.data) {
                is NavActionControl -> {
                    navNatinfs
                }

                is EnvActionData -> {
                    val envNatinfs: List<String> = action.data.infractions?.flatMap {
                        it.natinf.orEmpty()
                    }.orEmpty()
                    navNatinfs + envNatinfs
                }

                is FishActionData -> {
                    val fishNatinfs: List<String> = listOf(
                        action.data.gearInfractions.map { it.natinf.toString() },
                        action.data.logbookInfractions.map { it.natinf.toString() },
                        action.data.speciesInfractions.map { it.natinf.toString() },
                        action.data.otherInfractions.map { it.natinf.toString() }
                    ).flatten()

                    navNatinfs + fishNatinfs
                }

                else -> {
                    null
                }
            }
            val withReportCountNatinf =
                allNatinfs?.count { it != null }

            val natinfTag = if (withReportCountNatinf == 0) {
                "Sans infraction"
            } else {
                "$withReportCountNatinf NATINF"
            }

            return listOf(infractionTypeTag, natinfTag)
        }

        return null
    }

}
