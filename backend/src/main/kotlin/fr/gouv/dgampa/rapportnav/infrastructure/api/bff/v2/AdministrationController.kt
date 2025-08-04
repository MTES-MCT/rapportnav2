package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.administrations.GetAdministrationById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.administrations.GetAdministrations
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.env.FullAdministration
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v2/administrations")
class AdministrationController(
    private val getAdministrations: GetAdministrations,
    private val getAdministrationById: GetAdministrationById
) {

    private val logger = LoggerFactory.getLogger(AdministrationController::class.java)

    @GetMapping("")
    @Operation(summary = "Get the list of administration")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found administration", content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = FullAdministration::class)))
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Did not find any administration", content = [Content()])
        ]
    )
    fun getAll(): List<FullAdministration>? {
        try {
            return getAdministrations.execute()?: listOf()
        } catch (e: Exception) {
            logger.error("Error while fetching administrations : ${e.message}")
            return listOf()
        }
    }

    @GetMapping("{administrationId}")
    @Operation(summary = "Get administration by id")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found administration", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = FullAdministration::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Did not find any administration", content = [Content()])
        ]
    )
    fun get(@PathVariable administrationId: Int): FullAdministration? {
        try {
            return getAdministrationById.execute(administrationId)
        } catch (e: Exception) {
            logger.error("Error while fetching administration with id $administrationId : ${e.message}")
            return null
        }
    }
}
