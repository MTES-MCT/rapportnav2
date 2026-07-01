package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetCountries
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.CountryMapper
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Country
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
@RequestMapping("/api/v2/countries")
class CountryRestController(
    private val getCountries: GetCountries
) {

    @GetMapping
    @Operation(summary = "Get country list filtered")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found list of countries", content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = Country::class)))
                    ))
                ]
            ),
            ApiResponse(responseCode = "500", description = "Internal server error", content = [Content()])
        ]
    )
    fun getCountries(): List<Country> {
        return CountryMapper.toCountries(getCountries.execute())
    }
}

