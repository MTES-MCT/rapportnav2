package fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.v1

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeNavActionListByMissionId
import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.v1.adapters.outputs.ApiMissionDataOutput
import io.swagger.v3.oas.annotations.Operation
import jakarta.websocket.server.PathParam
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/missions")
class ApiMissionController(
    private val getComputeNavActionListByMissionId: GetComputeNavActionListByMissionId,
    private val missionNavRepository: IMissionNavRepository,
) {
    @GetMapping("/{missionId}")
    @Operation(
        summary = "Get data for a specific mission",
    )
    fun getMissionById(
        // missionId is the MonitorEnv external id (Int) in this public API's jargon.
        @PathVariable @PathParam("ID of a Mission")
        missionId: Int,
    ): ApiMissionDataOutput? {
        // Resolve the external id to the local mission (UUID) the nav actions are owned by.
        val mission = missionNavRepository.findByExternalId(missionId.toString()).orElse(null)
            ?: return ApiMissionDataOutput(
                id = missionId.toString(),
                containsActionsAddedByUnit = false
            )

        val navActions: List<MissionNavActionEntity> = getComputeNavActionListByMissionId.execute(ownerId = mission.id)

        return ApiMissionDataOutput(
            id = missionId.toString(),
            containsActionsAddedByUnit = navActions.isNotEmpty()
        )
    }

}
