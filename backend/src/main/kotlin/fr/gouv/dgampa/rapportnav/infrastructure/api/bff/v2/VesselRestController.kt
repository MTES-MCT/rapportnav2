package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetVessels
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Vessel
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

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
    fun getVessels(@RequestParam search: String): List<Vessel>? {
        return try {
            getVessels.execute()
                .filter { it.vesselName?.startsWith(search, ignoreCase = true) == true }
                .sortedBy { it.vesselName }
                .map { Vessel.fromVesselEntity(it) }
        } catch (e: Exception) {
            logger.error("[ERROR] API on endpoint getVessels:", e)
            null
        }
    }

    @GetMapping("{vesselId}")
    @Operation(summary = "get vessel")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "get vessel by id", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = Vessel::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Could not get a vessel by id", content = [Content()])
        ]
    )
    fun getVesselById(@PathVariable(name = "vesselId") vesselId: Int): Vessel? {
        return try {
            getVessels.execute()
                .find { it.vesselId == vesselId }
                ?.let { Vessel.fromVesselEntity(it) }
        } catch (e: Exception) {
            logger.error("[ERROR] API on endpoint getVessel:", e)
            null
        }
    }
}

