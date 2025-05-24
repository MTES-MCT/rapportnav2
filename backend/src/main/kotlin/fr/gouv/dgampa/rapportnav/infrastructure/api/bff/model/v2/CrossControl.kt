package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.*
import java.time.Instant
import java.util.*

class CrossControl(
    val id: UUID? = null,
    val agentId: String? = null,
    val vesselId: Int? = null,
    val serviceId: String? = null,
    var endDateTimeUtc: Instant? = null,
    val startDateTimeUtc: Instant? = null,
    val type: String? = null,
    val nbrOfHours: Int? = null,
    val origin: CrossControlOriginType? = null,
    val status: CrossControlStatusType? = null,
    val conclusion: CrossControlConclusionType? = null,
    val isSignedByInspector: Boolean? = null
) {

    fun toCrossControlEntity(): CrossControlEntity {
        return CrossControlEntity(
            id = id?: UUID.randomUUID(),
            type = type,
            status = status,
            origin = origin,
            agentId = agentId,
            vesselId = vesselId,
            serviceId = serviceId,
            nbrOfHours = nbrOfHours,
            conclusion = conclusion,
            endDateTimeUtc = endDateTimeUtc,
            isSignedByInspector = isSignedByInspector,
            startDateTimeUtc = startDateTimeUtc?: Instant.now()
        )
    }
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
                nbrOfHours = entity?.nbrOfHours,
                endDateTimeUtc = entity?.endDateTimeUtc,
                startDateTimeUtc = entity?.startDateTimeUtc,
                isSignedByInspector = entity?.isSignedByInspector
            )
        }
    }
}
