package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.administrations.GetAdministrationById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.administrations.GetAdministrations
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.env.FullAdministration
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
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
    fun getAll(): List<FullAdministration> {
        return getAdministrations.execute()
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
    fun get(@PathVariable administrationId: Int): FullAdministration {
        return getAdministrationById.execute(administrationId)
            ?: throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "AdministrationController.get: administration not found for id=$administrationId"
            )
    }
}
