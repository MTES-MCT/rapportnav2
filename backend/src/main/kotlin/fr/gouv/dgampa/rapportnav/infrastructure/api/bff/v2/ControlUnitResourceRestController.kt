package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.controlUnitResource.GetControlUnitResources
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.env.ControlUnitResourceEnv
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v2/resources")
class ControlUnitResourceRestController(
    private val getControlUnitResources: GetControlUnitResources
) {
    private val logger = LoggerFactory.getLogger(ControlUnitResourceRestController::class.java)
    @GetMapping
    @Operation(summary = "Get the list of control units")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found list of control units", content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = ControlUnitResourceEnv::class)))
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Did not find any control unit list", content = [Content()])
        ]
    )
    fun getAll(): List<ControlUnitResourceEnv>? {
        try {
            return getControlUnitResources.execute()?: listOf()
        } catch (e: Exception) {
            logger.error("Error while fetching Control unit : ${e.message}")
            return listOf()
        }
    }

}
