package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.*
import fr.gouv.dgampa.rapportnav.domain.utils.isValidUUID
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnvAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionFishAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavAction
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
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
    private val getStatusForAction: GetStatusForAction
) {

    @GetMapping("")
    @Operation(summary = "Get the list of actions on a owner id")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found actions", content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = MissionAction::class)))
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Did not find any actions", content = [Content()])
        ]
    )
    fun getActions(@PathVariable(name = "ownerId") ownerId: String): List<MissionAction?> {
        val actions = if (isValidUUID(ownerId)) {
            getMissionAction.execute(missionIdUUID = UUID.fromString(ownerId))
        } else {
            val missionId = ownerId.toIntOrNull()
                ?: throw BackendUsageException(
                    code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                    message = "Invalid ownerId: '$ownerId' is not a valid UUID or integer"
                )
            getMissionAction.execute(missionId = missionId)
        }
        return actions.map { action -> MissionAction.fromMissionActionEntity(action) }
    }

    @GetMapping("{actionId}")
    @Operation(summary = "Get the action by given id")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found action", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = MissionAction::class)
                    ))
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Did not find any action with that id",
                content = [Content()]
            )
        ]
    )
    fun getActionById(
        @PathVariable(name = "ownerId") ownerId: String,
        @PathVariable(name = "actionId") actionId: String,
    ): MissionAction? {
        var action: MissionAction? = null
        val navAction = getNavActionById.execute(actionId = actionId)
        if (navAction != null) action = MissionAction.fromMissionActionEntity(navAction)

        if (isValidUUID(ownerId)) return action

        val missionId = ownerId.toIntOrNull()
            ?: throw BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "Invalid ownerId: '$ownerId' is not a valid UUID or integer"
            )

        val fishAction = getFishActionById.execute(missionId = missionId, actionId = actionId)
        if (fishAction != null) action = MissionAction.fromMissionActionEntity(fishAction)

        val envAction = getEnvActionById.execute(missionId = missionId, actionId = actionId)
        if (envAction != null) action = MissionAction.fromMissionActionEntity(envAction)

        if (action != null) {
            action.status = getStatusForAction.execute(
                missionId = missionId,
                actionStartDateTimeUtc = action.data?.startDateTimeUtc
            )
        }
        return action
    }

    @PostMapping("")
    @Operation(summary = "Create a new action")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found action", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = MissionAction::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Did not Create an action", content = [Content()])
        ]
    )
    fun createAction(
        @PathVariable(name = "ownerId") ownerId: String,
        @RequestBody body: MissionNavAction
    ): MissionAction? {
        createNavAction.execute(body)
        return MissionAction.fromMissionActionEntity(getNavActionById.execute(actionId = body.id))
    }

    @PutMapping("{actionId}")
    @Operation(summary = "Update an existing action")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found action", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = MissionAction::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Did not update an action", content = [Content()])
        ]
    )
    fun updateAction(
        @PathVariable(name = "ownerId") ownerId: String,
        @PathVariable(name = "actionId") actionId: String,
        @RequestBody body: MissionAction
    ): MissionAction? {
        val response = when (body.source) {
            MissionSourceEnum.RAPPORTNAV -> updateNavAction.execute(actionId, body as MissionNavAction)
            MissionSourceEnum.MONITORENV -> updateEnvAction.execute(actionId, body as MissionEnvAction)
            MissionSourceEnum.MONITORFISH -> updateFishAction.execute(actionId, body as MissionFishAction)
            else -> throw BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "Unknown mission action source: ${body.source}"
            )
        }
        return MissionAction.fromMissionActionEntity(response)
    }

    @DeleteMapping("{actionId}")
    @Operation(summary = "Delete an action")
    @ApiResponse(responseCode = "404", description = "Did not delete the action", content = [Content()])
    fun deleteAction(@PathVariable(name = "actionId") actionId: String, @PathVariable ownerId: String) {
        if (!isValidUUID(actionId)) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "Invalid actionId: '$actionId' is not a valid UUID"
            )
        }
        deleteNavAction.execute(UUID.fromString(actionId))
    }
}
