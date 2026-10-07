package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.inquiry.*
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetUserFromToken
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Inquiry
import fr.gouv.dgampa.rapportnav.infrastructure.api.security.CheckMissionAccess
import fr.gouv.dgampa.rapportnav.infrastructure.api.security.MissionAccessResource
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.*

@RestController
@RequestMapping("/api/v2/inquiries")
class InquiryRestController(
    private val getUserFromToken: GetUserFromToken,
    private val processInquiry: ProcessInquiry,
    private val getInquiryById: GetInquiryById,
    private val deleteInquiry: DeleteInquiry,
    private val createInquiry: CreateInquiry,
    private val updateInquiry: UpdateInquiry,
    private val getInquiryByServiceIdBetweenDates: GetInquiryByServiceIdBetweenDates,
) {

    private val logger = LoggerFactory.getLogger(InquiryRestController::class.java)

    @GetMapping
    @Operation(summary = "Get the list of inquiry for a user service")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found list of inquiry", content = [
                    (Content(
                        mediaType = "application/json",
                        array = (ArraySchema(schema = Schema(implementation = Inquiry::class)))
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Did not find any list of inquiry", content = [Content()])
        ]
    )
    fun getInquiries(
        @RequestParam startDateTimeUtc: Instant,
        @RequestParam(required = false) endDateTimeUtc: Instant? = null
    ): List<Inquiry>? {
        return try {

            val user = getUserFromToken.execute()
            getInquiryByServiceIdBetweenDates.execute(serviceId = user?.serviceId,
                startDateTimeUtc = startDateTimeUtc,
                endDateTimeUtc = endDateTimeUtc)
                .sortedBy { it.startDateTimeUtc }
                .map { processInquiry.execute(it) }
                .map { Inquiry.fromInquiryEntity(it) }
        } catch (e: Exception) {
            logger.error("[ERROR] API on endpoint getCrossControlByServiceId:", e)
            listOf()
        }
    }

    @GetMapping("{id}")
    @Operation(summary = "Get inquiry by id")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found an inquiry", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = Inquiry::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Did not find any inquiry", content = [Content()])
        ]
    )
    // Cloisonnement runs in the aspect, before the method body — so a denial surfaces as 403 instead of
    // being swallowed to null by the try/catch below.
    @CheckMissionAccess(param = "id", resource = MissionAccessResource.INQUIRY)
    fun getInquiry(
        @PathVariable(name = "id") id: String
    ): Inquiry? {
        return try {
            getInquiryById.execute(id = UUID.fromString(id))
                ?.let { processInquiry.execute(it) }
                ?.let { Inquiry.fromInquiryEntity(it) }
        } catch (e: Exception) {
            logger.error("[ERROR] API on endpoint getCrossControlByServiceId:", e)
            null
        }
    }

    @PostMapping("")
    @Operation(summary = "Create a new Inquiry")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "create an inquiry", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = Inquiry::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Could not create an inquiry", content = [Content()])
        ]
    )
    fun create(
        @RequestBody body: Inquiry
    ): Inquiry? {
        // Cloisonnement: the new inquiry is owned by the authenticated user's service, not the body's serviceId.
        val serviceId = getUserFromToken.execute()?.serviceId
        return try {
            Inquiry.fromInquiryEntity(createInquiry.execute(inquiry = body, serviceId = serviceId))
        } catch (e: Exception) {
            logger.error("Error while creating a new inquiry : ", e)
            return null
        }
    }

    @PutMapping("{inquiryId}")
    @Operation(summary = "Update an Inquiry")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "update an inquiry", content = [
                    (Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = Inquiry::class)
                    ))
                ]
            ),
            ApiResponse(responseCode = "404", description = "Could not update  inquiry", content = [Content()])
        ]
    )
    // Cloisonnement runs in the aspect, against the existing inquiry, before the method body — so a
    // denial surfaces as 403 instead of being swallowed to null by the try/catch below.
    @CheckMissionAccess(param = "inquiryId", resource = MissionAccessResource.INQUIRY)
    fun update(
        @PathVariable(name = "inquiryId") inquiryId: String,
        @RequestBody body: Inquiry
    ): Inquiry? {
        return try {
            Inquiry.fromInquiryEntity(
                updateInquiry.execute(
                    inquiry = body,
                    id = inquiryId.let { UUID.fromString(it) }
                )
            )
        } catch (e: Exception) {
            logger.error("Error while creating a new inquiry : ", e)
            return null
        }
    }

    @DeleteMapping("{inquiryId}")
    @Operation(summary = "Delete an inquiry")
    @ApiResponse(responseCode = "404", description = "Could not delete  inquiry", content = [Content()])
    @CheckMissionAccess(param = "inquiryId", resource = MissionAccessResource.INQUIRY)
    fun delete(@PathVariable(name = "inquiryId") inquiryId: String) {
        return deleteInquiry.execute(id = UUID.fromString(inquiryId))
    }
}
