package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetInquiryById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetInquiryByServiceId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.ProcessInquiry
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetUserFromToken
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Inquiry
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/v2/inquiries")
class InquiryRestController(
    private val getUserFromToken: GetUserFromToken,
    private val processInquiry: ProcessInquiry,
    private val getInquiryById: GetInquiryById,
    private val getInquiryByServiceId: GetInquiryByServiceId,
) {

    private val logger = LoggerFactory.getLogger(InquiryRestController::class.java)

    @GetMapping
    fun getAll(): List<Inquiry>? {
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
    fun getById(
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
}
