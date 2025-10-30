package fr.gouv.dgampa.rapportnav.infrastructure.api.admin

import fr.gouv.dgampa.rapportnav.domain.use_cases.service.CreateOrUpdateService
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.DeleteService
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServices
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.Service
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v2/admin/services")
class ServiceAdminController(
    private val getServices: GetServices,
    private val deleteService: DeleteService,
    private val getServiceById: GetServiceById,
    private val createOrUpdateService: CreateOrUpdateService
) {
    private val logger = LoggerFactory.getLogger(ServiceAdminController::class.java)

    @GetMapping("")
    @Operation(summary = "Get the list of services for a specific user")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Get Services", content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = Service::class)))
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Did not find any services", content = [Content()])
        ]
    )
    fun getServices(): List<Service?>? {
        return try {
            getServices.execute().map { Service.fromServiceEntity((it)) }
        } catch (e: Exception) {
            logger.error("ServiceRestController - failed to load services from MonitorEnv", e)
            throw Exception(e)
        }
    }

    @GetMapping("{serviceId}")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found service by id", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = Service::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Did not find any service", content = [Content()])
        ]
    )
    fun getServiceById(
        @PathVariable(name = "serviceId") serviceId: Int
    ): Service? {
        return try {
            getServiceById.execute(serviceId)?.let { Service.fromServiceEntity(it) }
        } catch (e: Exception) {
            logger.error("Error while creating MonitorEnv service : ", e)
            return null
        }
    }


    @PostMapping("")
    @Operation(summary = "Create Service")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Create service", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = Service::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Could not create no service", content = [Content()])
        ]
    )
    fun create(@RequestBody body: Service): Service? {
        return try {
            createOrUpdateService.execute(body.toServiceEntity()).let { Service.fromServiceEntity(it) }
        } catch (e: Exception) {
            logger.error("Error while creating MonitorEnv service : ", e)
            return null
        }
    }

    @PutMapping("")
    @Operation(summary = "Update Service")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Create service", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = Service::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Could not create no service", content = [Content()])
        ]
    )
    fun update(
        @RequestBody body: Service
    ): Service? {
        return try {
            createOrUpdateService.execute(entity = body.toServiceEntity()).let { Service.fromServiceEntity(it) }
        } catch (e: Exception) {
            logger.error("Error while creating service : ", e)
            return null
        }
    }

    @DeleteMapping("{serviceId}")
    @Operation(summary = "Delete a service create by the unity")
    @ApiResponse(responseCode = "404", description = "Could not delete service", content = [Content()])
    fun delete(
        @PathVariable(name = "serviceId") serviceId: Int
    ) {
        return try {
            deleteService.execute(id = serviceId)
        } catch (e: Exception) {
            logger.error("Error while creating MonitorEnv service : ", e)
        }
    }
}
