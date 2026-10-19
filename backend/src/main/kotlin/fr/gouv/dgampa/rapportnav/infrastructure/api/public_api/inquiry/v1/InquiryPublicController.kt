package fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.inquiry.v1

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.inquiry.GetInquiriesByVesselId
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServices
import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.inquiry.v1.adapters.output.InquiryOutput
import fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.inquiry.v1.adapters.output.InquiryOutputMapper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/api/v1/public/inquiries")
@Tag(name = "Inquiry", description = "Public API for inquiries (contrôles croisés)")
class InquiryPublicController(
    private val getInquiriesByVesselId: GetInquiriesByVesselId,
    private val getServices: GetServices
) {

    @GetMapping
    @Operation(
        summary = "List inquiries of a vessel",
        description = "Inquiries of a vessel started after startDateTimeUtc (default: start of the year, 5 years ago), most recent first.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Inquiries successfully retrieved",
                content = [Content(array = ArraySchema(schema = Schema(implementation = InquiryOutput::class)))]
            )
        ]
    )
    fun getInquiriesByVesselId(
        @Parameter(description = "NAVPRO vessel id")
        @RequestParam vesselId: Int,
        @Parameter(description = "ISO-8601 UTC instant, e.g. 2021-01-01T00:00:00Z")
        @RequestParam(required = false) startDateTimeUtc: Instant?
    ): List<InquiryOutput> {
        val inquiries = getInquiriesByVesselId.execute(vesselId = vesselId, startDateTimeUtc = startDateTimeUtc)
        if (inquiries.isEmpty()) return emptyList()

        val servicesById = findServicesById()
        return inquiries.map { InquiryOutputMapper.toOutput(it, servicesById) }
    }

    private fun findServicesById(): Map<Int, ServiceEntity> {
        return getServices.execute()
            .mapNotNull { service -> service.id?.let { it to service } }
            .toMap()
    }
}
