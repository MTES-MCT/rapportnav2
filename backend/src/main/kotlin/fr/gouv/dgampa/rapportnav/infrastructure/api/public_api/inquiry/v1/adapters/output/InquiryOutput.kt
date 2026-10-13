package fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.inquiry.v1.adapters.output

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryConclusionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryOriginType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryStatusType
import java.time.Instant
import java.util.*

data class InquiryOutput(
    val id: UUID,
    val vesselId: Int?,
    val type: String? = null,
    val origin: InquiryOriginType? = null,
    val status: InquiryStatusType? = null,
    val conclusion: InquiryConclusionType? = null,
    val startDateTimeUtc: Instant? = null,
    val endDateTimeUtc: Instant? = null,
    val service: InquiryServiceOutput? = null
)

data class InquiryServiceOutput(
    val id: Int,
    val name: String?
)
