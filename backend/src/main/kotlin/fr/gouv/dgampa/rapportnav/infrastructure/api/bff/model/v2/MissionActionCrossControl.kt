package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.*
import java.time.Instant
import java.util.*

class MissionActionCrossControl(
    val id: UUID? = null,
    val agentId: Int? = null,
    val vesselId: Int? = null,
    val serviceId: Int? = null,
    val type: String? = null,
    val nbrOfHours: Int? = null,
    val isRefentialClosed: Boolean?  = null,
    val origin: CrossControlOriginType? = null,
    val status: CrossControlStatusType? = null,
    val conclusion: CrossControlConclusionType? = null,
    val isSignedByInspector: Boolean? = null
) {

    fun toCrossControlEntity(startDateTimeUtc: Instant?, endDateTimeUtc: Instant?,): CrossControlEntity {
        return CrossControlEntity(
            id = id,
            type = type,
            status = status,
            origin = origin,
            agentId = agentId,
            vesselId = vesselId,
            serviceId = serviceId,
            conclusion = conclusion,
            endDateTimeUtc = endDateTimeUtc,
            startDateTimeUtc = startDateTimeUtc
        )
    }

    fun toMissionActionCrossControlEntity(): MissionActionCrossControlEntity {
        return MissionActionCrossControlEntity(
            id = id,
            status = status,
            nbrOfHours = nbrOfHours,
            conclusion = conclusion,
            isSignedByInspector = isSignedByInspector
        )
    }

    companion object {
        fun fromCrossControlEntity(entity: MissionActionCrossControlEntity?): MissionActionCrossControl {
            return MissionActionCrossControl(
                id = entity?.id,
                status = entity?.status,
                conclusion = entity?.conclusion,
                nbrOfHours = entity?.nbrOfHours,
                type = entity?.crossControlData?.type,
                origin = entity?.crossControlData?.origin,
                agentId = entity?.crossControlData?.agentId,
                vesselId = entity?.crossControlData?.vesselId,
                serviceId = entity?.crossControlData?.serviceId,
                isSignedByInspector = entity?.isSignedByInspector,
                isRefentialClosed = entity?.crossControlData?.isRefentialClosed
            )
        }
    }
}
