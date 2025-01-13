package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.*
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnvAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionFishAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavAction
import io.swagger.v3.oas.annotations.Operation
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v2/missions/{missionId}/actions")
class MissionActionRestController(
    private val deleteNavAction: DeleteNavAction,
    private val createNavAction: CreateNavAction,
    private val updateEnvAction: UpdateEnvAction,
    private val updateNavAction: UpdateNavAction,
    private val updateFishAction: UpdateFishAction,
    private val getNavActionById: GetNavActionById,
    private val getEnvActionById: GetEnvActionById,
    private val getFishActionById: GetFishActionById,
    private val getEnvActionByMissionId: GetEnvActionListByMissionId,
    private val getNavActionByMissionId: GetNavActionListByMissionId,
    private val getFIshListActionByMissionId: GetFishActionListByMissionId,
) {
    private val logger = LoggerFactory.getLogger(MissionActionRestController::class.java)

    @GetMapping("")
    @Operation(summary = "Get the list of actions on a mission Id")
    fun getActions(@PathVariable(name = "missionId") missionId: Int): List<MissionAction?> {
        val envActions = getEnvActionByMissionId.execute(missionId = missionId).orEmpty()
        val navActions = getNavActionByMissionId.execute(missionId = missionId).orEmpty()
        val fishActions = getFIshListActionByMissionId.execute(missionId = missionId).orEmpty()
        return (envActions + navActions + fishActions)
            .sortedByDescending { action -> action.startDateTimeUtc }
            .map { action -> MissionAction.fromMissionActionEntity(action) }
    }

    @GetMapping("{actionId}")
    fun getActionById(
        @PathVariable(name = "missionId") missionId: Int,
        @PathVariable(name = "actionId") actionId: String,
    ): MissionAction? {
        val navAction = getNavActionById.execute(actionId = actionId)
        if (navAction != null) return MissionAction.fromMissionActionEntity(navAction)

        val fishAction = getFishActionById.execute(missionId = missionId, actionId = actionId)
        if (fishAction != null) return MissionAction.fromMissionActionEntity(fishAction)

        val envAction = getEnvActionById.execute(missionId = missionId, actionId = actionId) ?: return null
        return MissionAction.fromMissionActionEntity(envAction)
    }

    @PostMapping("")
    @Operation(summary = "Create a new mission action")
    fun createAction(@RequestBody body: MissionNavAction): MissionAction? {
        return MissionAction.fromMissionActionEntity(createNavAction.execute(body))
    }

    @PutMapping("{actionId}")
    @Operation(summary = "Create a new mission action")
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
    fun deleteAction(@PathVariable(name = "actionId") actionId: String, @PathVariable missionId: String): Unit {
        deleteNavAction.execute(UUID.fromString(actionId))
    }
}
