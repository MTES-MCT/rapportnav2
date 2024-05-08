package fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.v1

import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetMissionById
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
    private val getMissionById: GetMissionById,
) {
    @GetMapping("/{missionId}")
    @Operation(
        summary = "Get data for a specific mission",
    )
    fun getMissionById(
        @PathParam("ID of a Mission")
        @PathVariable(name = "missionId")
        missionId: Int,
    ): ApiMissionDataOutput? {
        val mission: MissionEntity? = getMissionById.execute(missionId = missionId)
        return ApiMissionDataOutput.fromMissionEntity(mission)
    }

}
