package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.inquiry.*
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetUserFromToken
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Inquiry
import io.swagger.v3.oas.annotations.Operation
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
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
    private val getInquiryByServiceId: GetInquiryByServiceId,
) {

    private val logger = LoggerFactory.getLogger(InquiryRestController::class.java)

    @GetMapping
    fun getInquiries(): List<Inquiry>? {
        return try {
            val user = getUserFromToken.execute()
            getInquiryByServiceId.execute(serviceId = user?.serviceId)
                .sortedBy { it.startDateTimeUtc }
                .map { processInquiry.execute(it) }
                .map { Inquiry.fromInquiryEntity(it) }
        } catch (e: Exception) {
            logger.error("[ERROR] API on endpoint getCrossControlByServiceId:", e)
            null
        }
    }

    @GetMapping("{id}")
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
    fun create(
        @RequestBody body: Inquiry
    ): Inquiry? {
        return try {
            Inquiry.fromInquiryEntity(createInquiry.execute(inquiry = body))
        } catch (e: Exception) {
            logger.error("Error while creating a new inquiry : ", e)
            return null
        }
    }

    @PutMapping("{inquiryId}")
    @Operation(summary = "Update an Inquiry")
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
    fun delete(@PathVariable(name = "inquiryId") inquiryId: String) {
        return deleteInquiry.execute(id = UUID.fromString(inquiryId))
    }
}
