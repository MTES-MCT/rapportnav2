package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.fish.GetFishAuctions
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.FishAuction
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2/fish_auctions")
class FishAuctionController(
    private val getFishAuctions: GetFishAuctions
) {

    @GetMapping("")
    @Operation(summary = "Get the list of fish auctions")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found list of fish auctions", content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = FishAuction::class)))
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Did not find any fish auctions", content = [Content()])
        ]
    )
    fun getFishAuctions(): List<FishAuction> {
        return getFishAuctions.execute().map { FishAuction.fromFishAuctionEntity(it) }
    }
}
