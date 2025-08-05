package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.UpdateGeneralInfo
import fr.gouv.dgampa.rapportnav.domain.utils.isValidUUID
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v2/missions/{missionId}/general_infos")
class MissionGeneralInfoRestController(
    private val updateGeneralInfo: UpdateGeneralInfo
)
{
    private val logger = LoggerFactory.getLogger(MissionGeneralInfoRestController::class.java)
    @PutMapping
    @Operation(summary = "Update general information, by mission id")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "update general information's", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = MissionGeneralInfoEntity2::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Could not update general information's", content = [Content()])
        ]
    )
    fun update(
        @PathVariable missionId: String,
        @RequestBody generalInfo: MissionGeneralInfo2
    ): MissionGeneralInfoEntity2? {
        return if (isValidUUID(missionId))
            updateGeneralInfo.execute(missionIdUUID = UUID.fromString(missionId), generalInfo = generalInfo)
        else updateGeneralInfo.execute(
            missionId = Integer.valueOf(missionId), generalInfo = generalInfo
        )
    }
}
