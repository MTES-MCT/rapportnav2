package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import java.time.Instant
import java.util.*

class MissionActionCrossControlEntity(
    val id: UUID? = null,
    val type: String? = null,
    val nbrOfHours: Int? = null,
    val agentId: String? = null,
    val vesselId: Int? = null,
    val serviceId: Int? = null,
    var endDateTimeUtc: Instant? = null,
    val startDateTimeUtc: Instant? = null,
    val origin: CrossControlOriginType? = null,
    val isSignedByInspector: Boolean? = null,
    val status: CrossControlStatusType? = null,
    val conclusion: CrossControlConclusionType? = null,
) {

    fun toCrossControlEntity(): CrossControlEntity {
        return CrossControlEntity(
            id = id ?: UUID.randomUUID(),
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

    companion object {
        fun fromNavActionEntityAndCrossControlEntity(
            actionEntity: MissionNavActionEntity,
            crossControlEntity: CrossControlEntity?
        ): MissionActionCrossControlEntity {
            return MissionActionCrossControlEntity(
                id = crossControlEntity?.id,
                type = crossControlEntity?.type,
                origin = crossControlEntity?.origin,
                agentId = crossControlEntity?.agentId,
                vesselId = crossControlEntity?.vesselId,
                serviceId = crossControlEntity?.serviceId,
                status = actionEntity.crossControl?.status,
                nbrOfHours = actionEntity.crossControl?.nbrOfHours,
                conclusion = actionEntity.crossControl?.conclusion,
                endDateTimeUtc = crossControlEntity?.endDateTimeUtc,
                startDateTimeUtc = crossControlEntity?.startDateTimeUtc,
                isSignedByInspector = actionEntity.crossControl?.isSignedByInspector
            )
        }
    }
}
