package fr.gouv.dgampa.rapportnav.infrastructure.api.bff

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum.CONTROL
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum.SURVEILLANCE
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.InfractionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.FakeMissionData
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetFishActionsByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.*
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.GetInfractionsByActionId
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.action.*
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action.*
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.infraction.Infraction
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
    private val getFishActionsByMissionId: GetFishActionsByMissionId,
    private val getFishActionByIdAndMissionId: GetFishActionByIdAndMissionId,
    private val getEnvActionByIdAndMissionId: GetEnvActionByIdAndMissionId,
    private val getNavActionByIdAndMissionId: GetNavActionByIdAndMissionId,
    private val getInfractionsByActionId: GetInfractionsByActionId,
    private val fakeMissionData: FakeMissionData,
    private val addOrUpdateActionFreeNote: AddOrUpdateActionFreeNote,
    private val deleteActionFreeNote: DeleteActionFreeNote,
    private val addOrUpdateActionRescue: AddOrUpdateActionRescue,
    private val addOrUpdateActionNauticalEvent: AddOrUpdateActionNauticalEvent,
    private val addOrUpdateActionVigimer: AddOrUpdateActionVigimer,
    private val addOrUpdateActionAntiPollution: AddOrUpdateActionAntiPollution,
    private val addOrUpdateActionBAAEMPermanence: AddOrUpdateActionBAAEMPermanence,
    private val addOrUpdateActionPublicOrder: AddOrUpdateActionPublicOrder,
    private val addOrUpdateActionRepresentation: AddOrUpdateActionRepresentation,
    private val addOrUpdateActionIllegalImmigration: AddOrUpdateActionIllegalImmigration,
    private val deleteActionIllegalImmigration: DeleteActionIllegalImmigration,
    private val deleteActionVigimer: DeleteActionVigimer,
    private val deleteActionBAAEMPermanence: DeleteActionBAAEMPermanence,
    private val deleteActionRepresentation: DeleteActionRepresentation,
    private val deleteActionNauticalEvent: DeleteActionNauticalEvent,
    private val deleteActionPublicOrder: DeleteActionPublicOrder,
    private val deleteActionRescue: DeleteActionRescue,
    private val deleteActionAntiPollution: DeleteActionAntiPollution
) {

    private val logger =
        LoggerFactory.getLogger(fr.gouv.dgampa.rapportnav.infrastructure.api.bff.ActionController::class.java)


    @QueryMapping
    fun actionById(
        @Argument id: String,
        @Argument missionId: Int,
        @Argument source: MissionSourceEnum,
        @Argument type: ActionType
    ): Action? {
        if (id in fakeMissionData.getEnvActionIds()) {
            val fakeAction = fakeMissionData.envControls(missionId).first()
            return Action.fromEnvAction(
                envAction = fakeAction,
                missionId = missionId
            )
        } else if (id in fakeMissionData.getFishActionIds()) {
            val fakeAction =
                getFishActionsByMissionId.getFakeActions(missionId)
                    .first { it.controlAction?.action?.id.toString() == id }
            return Action.fromFishAction(
                fishAction = fakeAction,
                missionId = missionId
            )
        }

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
        val data = controlAction.toActionControlEntity()
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
        if (action.source == MissionSourceEnum.RAPPORTNAV && action.data is NavActionStatus) {
            return action.data.status
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
                is NavActionFreeNote -> ActionType.NOTE
                is NavActionRescue -> ActionType.RESCUE
                is NavActionNauticalEvent -> ActionType.NAUTICAL_EVENT
                is NavActionVigimer -> ActionType.VIGIMER
                is NavActionAntiPollution -> ActionType.ANTI_POLLUTION
                is NavActionBAAEMPermanence -> ActionType.BAAEM_PERMANENCE
                is NavActionPublicOrder -> ActionType.PUBLIC_ORDER
                is NavActionRepresentation -> ActionType.REPRESENTATION
                is NavActionIllegalImmigration -> ActionType.ILLEGAL_IMMIGRATION
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
            }.filterNotNull()

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
                allNatinfs?.count { true }

            val natinfTag = if (withReportCountNatinf == 0) {
                "Sans infraction"
            } else {
                "$withReportCountNatinf NATINF"
            }

            return listOf(infractionTypeTag, natinfTag)
        }

        return null
    }

    @MutationMapping
    fun addOrUpdateFreeNote(@Argument freeNoteAction: ActionFreeNoteInput): NavActionFreeNote {
        val data = freeNoteAction.toActionFreeNoteEntity()
        return addOrUpdateActionFreeNote.execute(data).toNavActionFreeNote()
    }

    @MutationMapping
    fun deleteFreeNote(@Argument id: UUID): Boolean {
        return deleteActionFreeNote.execute(id)
    }

    @MutationMapping
    fun addOrUpdateActionRescue(@Argument rescueAction: ActionRescueInput): NavActionRescue {
        val data = rescueAction.toActionRescueEntity()
        return addOrUpdateActionRescue.execute(data).toNavActionRescue()
    }

    @MutationMapping
    fun addOrUpdateActionNauticalEvent(@Argument nauticalAction: ActionNauticalEventInput): NavActionNauticalEvent {
        val data = nauticalAction.toActionNauticalEventEntity()
        return addOrUpdateActionNauticalEvent.execute(data).toNavActionNauticalEvent()
    }

    @MutationMapping
    fun addOrUpdateActionVigimer(@Argument vigimerAction: ActionVigimerInput): NavActionVigimer {
        val data = vigimerAction.toActionVigimerEntity()
        return addOrUpdateActionVigimer.execute(data).toNavActionVigimer()
    }

    @MutationMapping
    fun addOrUpdateActionAntiPollution(@Argument antiPollutionAction: ActionAntiPollutionInput): NavActionAntiPollution {
        val data = antiPollutionAction.toActionAntiPollutionEntity()
        return addOrUpdateActionAntiPollution.execute(data).toNavActionAntiPollution()
    }

    @MutationMapping
    fun addOrUpdateActionBAAEMPermanence(@Argument baaemPermanenceAction: ActionBAAEMPermanenceInput): NavActionBAAEMPermanence {
        val data = baaemPermanenceAction.toActionBAAEMPermanenceEntity()
        return addOrUpdateActionBAAEMPermanence.execute(data).toNavActionBAAEMPermanence()
    }

    @MutationMapping
    fun addOrUpdateActionPublicOrder(@Argument publicOrderAction: ActionPublicOrderInput): NavActionPublicOrder {
        val data = publicOrderAction.toActionPublicOrderEntity()
        return addOrUpdateActionPublicOrder.execute(data).toNavActionPublicOrder()
    }

    @MutationMapping
    fun addOrUpdateActionRepresentation(@Argument representationAction: ActionRepresentationInput): NavActionRepresentation {
        val data = representationAction.toActionRepresentationEntity()
        return addOrUpdateActionRepresentation.execute(data).toNavActionRepresentation()
    }

    @MutationMapping
    fun addOrUpdateActionIllegalImmigration(@Argument illegalImmigrationAction: ActionIllegalImmigrationInput): NavActionIllegalImmigration {
        val data = illegalImmigrationAction.toActionIllegalImmigrationEntity()
        return addOrUpdateActionIllegalImmigration.execute(data).toNavActionIllegalImmigration()
    }

    @MutationMapping
    fun deleteRescue(@Argument id: UUID): Boolean {
        return deleteActionRescue.execute(id)
    }

    @MutationMapping
    fun deleteNauticalEvent(@Argument id: UUID): Boolean {
        return deleteActionNauticalEvent.execute(id)
    }

    @MutationMapping
    fun deleteVigimer(@Argument id: UUID): Boolean {
        return deleteActionVigimer.execute(id)
    }

    @MutationMapping
    fun deleteBAAEMPermanence(@Argument id: UUID): Boolean {
        return deleteActionBAAEMPermanence.execute(id)
    }

    @MutationMapping
    fun deleteAntiPollution(@Argument id: UUID): Boolean {
        return deleteActionAntiPollution.execute(id)
    }

    @MutationMapping
    fun deleteRepresentation(@Argument id: UUID): Boolean {
        return deleteActionRepresentation.execute(id)
    }

    @MutationMapping
    fun deletePublicOrder(@Argument id: UUID): Boolean {
        return deleteActionPublicOrder.execute(id)
    }

    @MutationMapping
    fun deleteIllegalImmigration(@Argument id: UUID): Boolean {
        return deleteActionIllegalImmigration.execute(id)
    }


}
