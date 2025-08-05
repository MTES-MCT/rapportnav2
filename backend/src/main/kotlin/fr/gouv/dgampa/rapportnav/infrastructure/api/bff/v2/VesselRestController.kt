package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetVessels
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Vessel
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2/vessels")
class VesselRestController(
    private val getVessels: GetVessels
) {
    private val logger = LoggerFactory.getLogger(VesselRestController::class.java)

    @GetMapping
    @Operation(summary = "get vessel list")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "get vessel list", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = Vessel::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Could not get no vessel", content = [Content()])
        ]
    )
    fun getVessels(): List<Vessel>? {
        return try {
            getVessels.execute().sortedBy { it.vesselName }.map { Vessel.fromVesselEntity(it) }
        } catch (e: Exception) {
            logger.error("[ERROR] API on endpoint getVessels:", e)
            null
        }
    }
}

