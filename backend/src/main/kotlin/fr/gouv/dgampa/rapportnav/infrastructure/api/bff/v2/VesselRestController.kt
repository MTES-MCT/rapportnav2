package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetVessels
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Vessel
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2/vessels")
class VesselRestController(
    private val getVessels: GetVessels
) {

    @GetMapping
    @Operation(summary = "Get vessel list filtered by search query")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found list of vessels", content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = Vessel::class)))
                    ))
                ]
            ),
            ApiResponse(responseCode = "500", description = "Internal server error", content = [Content()])
        ]
    )
    fun getVessels(@RequestParam search: String): List<Vessel> {
        return getVessels.execute()
            .filter { it.vesselName?.startsWith(search, ignoreCase = true) == true }
            .sortedBy { it.vesselName }
            .map { Vessel.fromVesselEntity(it) }
    }

    @GetMapping("{vesselId}")
    @Operation(summary = "Get vessel by id")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found vessel by id", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = Vessel::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "500", description = "Internal server error", content = [Content()])
        ]
    )
    fun getVesselById(@PathVariable(name = "vesselId") vesselId: Int): Vessel? {
        return getVessels.execute()
            .find { it.vesselId == vesselId }
            ?.let { Vessel.fromVesselEntity(it) }
    }
}

