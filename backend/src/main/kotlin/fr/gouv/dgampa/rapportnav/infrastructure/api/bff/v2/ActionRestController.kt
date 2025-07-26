package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.*
import fr.gouv.dgampa.rapportnav.domain.utils.isValidUUID
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnvAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionFishAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavAction
import io.swagger.v3.oas.annotations.Operation
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v2/owners/{ownerId}/actions")
class ActionRestController(
    private val getMissionAction: GetMissionAction,
    private val deleteNavAction: DeleteNavAction,
    private val createNavAction: CreateNavAction,
    private val updateEnvAction: UpdateEnvAction,
    private val updateNavAction: UpdateNavAction,
    private val updateFishAction: UpdateFishAction,
    private val getNavActionById: GetNavActionById,
    private val getEnvActionById: GetEnvActionById,
    private val getFishActionById: GetFishActionById,
    private val getStatusForAction2: GetStatusForAction2
) {
    private val logger = LoggerFactory.getLogger(ActionRestController::class.java)

    @GetMapping("")
    @Operation(summary = "Get the list of actions on a mission Id")
    fun getActions(@PathVariable(name = "ownerId") ownerId: String): List<MissionAction?> {
        val actions = if (isValidUUID(ownerId)) getMissionAction.execute(
            missionIdUUID = UUID.fromString(ownerId)
        ) else getMissionAction.execute(
            missionId = Integer.valueOf(ownerId)
        )
        return actions.map { action -> MissionAction.fromMissionActionEntity(action) }
    }

    @GetMapping("{actionId}")
    fun getActionById(
        @PathVariable(name = "ownerId") ownerId: String,
        @PathVariable(name = "actionId") actionId: String,
    ): MissionAction? {
        var action: MissionAction? = null
        val navAction = getNavActionById.execute(actionId = actionId)
        if (navAction != null) action = MissionAction.fromMissionActionEntity(navAction)

        if (isValidUUID(ownerId)) return action

        val fishAction = getFishActionById.execute(missionId = Integer.valueOf(ownerId), actionId = actionId)
        if (fishAction != null) action =  MissionAction.fromMissionActionEntity(fishAction)

        val envAction = getEnvActionById.execute(missionId = Integer.valueOf(ownerId), actionId = actionId)
        if (envAction != null) action =  MissionAction.fromMissionActionEntity(envAction)

        if (action != null) {
            action.status = getStatusForAction2.execute(
                missionId = Integer.valueOf(ownerId),
                actionStartDateTimeUtc = action.data?.startDateTimeUtc
            )
        }
        return action
    }

    @PostMapping("")
    @Operation(summary = "Create a new mission action")
    fun createAction(@RequestBody body: MissionNavAction): MissionAction? {
        return MissionAction.fromMissionActionEntity(createNavAction.execute(body))
    }

    @PutMapping("{actionId}")
    @Operation(summary = "Update an existing mission action")
    fun updateAction(
        @PathVariable(name = "actionId") actionId: String,
        @RequestBody body: MissionAction
    ): MissionAction? {
        val response = when (body.source) {
            MissionSourceEnum.RAPPORTNAV -> updateNavAction.execute(actionId,  body as MissionNavAction)
            MissionSourceEnum.MONITORENV -> updateEnvAction.execute(actionId, body as MissionEnvAction)
            MissionSourceEnum.MONITORFISH -> updateFishAction.execute(actionId, body as MissionFishAction)
            else -> throw RuntimeException("Unknown mission action source: ${body.source}")
        }
        this.logger.info(body.id)
        return MissionAction.fromMissionActionEntity(response)
    }

    @DeleteMapping("{actionId}")
    @Operation(summary = "Delete a mission action")
    fun deleteAction(@PathVariable(name = "actionId") actionId: String, @PathVariable missionId: String) {
        deleteNavAction.execute(UUID.fromString(actionId))
    }
}
