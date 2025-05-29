package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.*
import java.time.Instant
import java.util.*

class CrossControl(
    val id: UUID? = null,
    val agentId: Int? = null,
    val vesselId: Int? = null,
    val serviceId: Int? = null,
    var endDateTimeUtc: Instant? = null,
    val startDateTimeUtc: Instant? = null,
    val type: String? = null,
    val sumNbrOfHours: Int? = null,
    val origin: CrossControlOriginType? = null,
    val status: CrossControlStatusType? = null,
    val conclusion: CrossControlConclusionType? = null,
    val vesselName: String? = null,
    val vesselExternalReferenceNumber: String? = null
) {
    companion object {
        fun fromCrossControlEntity(entity: CrossControlEntity?): CrossControl {
            return CrossControl(
                id = entity?.id,
                type = entity?.type,
                status = entity?.status,
                origin = entity?.origin,
                agentId = entity?.agentId,
                vesselId = entity?.vesselId,
                serviceId = entity?.serviceId,
                conclusion = entity?.conclusion,
                vesselName = entity?.vesselName,
                sumNbrOfHours =  entity?.sumNbrOfHours,
                endDateTimeUtc = entity?.endDateTimeUtc,
                startDateTimeUtc = entity?.startDateTimeUtc,
                vesselExternalReferenceNumber = entity?.vesselExternalReferenceNumber
            )
        }
    }
}
