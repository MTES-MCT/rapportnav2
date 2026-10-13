package fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.inquiry.v1.adapters.output

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryConclusionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryOriginType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryStatusType
import java.time.Instant
import java.util.*

data class InquiryOutput(
    val id: UUID,
    val vesselId: Int?,
    val missionId: Int? = null,
    val type: String? = null,
    val origin: InquiryOriginType? = null,
    val status: InquiryStatusType? = null,
    val conclusion: InquiryConclusionType? = null,
    val startDateTimeUtc: Instant? = null,
    val endDateTimeUtc: Instant? = null,
    val isSignedByInspector: Boolean? = null,
    val service: InquiryServiceOutput? = null,
    val infractions: List<InquiryInfractionOutput> = emptyList(),
    val observations: List<String> = emptyList()
)

data class InquiryServiceOutput(
    val id: Int,
    val name: String?,
    val controlUnitIds: List<Int> = emptyList()
)

data class InquiryInfractionOutput(
    val controlType: ControlType? = null,
    val infractionType: InfractionTypeEnum? = null,
    val natinfs: List<String> = emptyList(),
    val observations: String? = null
)
