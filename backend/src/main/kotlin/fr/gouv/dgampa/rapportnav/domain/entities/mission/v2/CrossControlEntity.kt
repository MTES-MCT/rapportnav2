package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2


import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.CrossControlModel
import java.time.Instant
import java.util.*

class CrossControlEntity(
    val id: UUID,
    val agentId: String? = null,
    val vesselId: Int? = null,
    val serviceId: Int? = null,
    var endDateTimeUtc: Instant? = null,
    val startDateTimeUtc: Instant? = null,
    val type: String? = null,
    val nbrOfHours: Int? = null,
    val isSignedByInspector: Boolean? = null,
    val origin: CrossControlOriginType? = null,
    val status: CrossControlStatusType? = null,
    val conclusion: CrossControlConclusionType? = null,
) {

    fun toCrossControlModel(): CrossControlModel {
        return CrossControlModel(
            id = id,
            type = type,
            agentId = agentId,
            serviceId = serviceId,
            vesselId = vesselId,
            status = status?.toString(),
            origin = origin?.toString(),
            endDateTimeUtc = endDateTimeUtc,
            startDateTimeUtc = startDateTimeUtc!!,
            conclusion = conclusion?.toString()
        )
    }

    companion object {
        fun fromCrossControlModel(model: CrossControlModel): CrossControlEntity {
            return CrossControlEntity(
                id = model.id,
                type = model.type,
                agentId = model.agentId,
                vesselId = model.vesselId,
                serviceId = model.serviceId,
                endDateTimeUtc = model.endDateTimeUtc,
                startDateTimeUtc = model.startDateTimeUtc,
                origin = model.origin?.let { CrossControlOriginType.valueOf(it) },
                status = model.status?.let { CrossControlStatusType.valueOf(it) },
                conclusion = model.conclusion?.let { CrossControlConclusionType.valueOf(it) },
            )
        }

        fun fromMissionNavActionEntity(entity: MissionNavActionEntity): CrossControlEntity {
            return CrossControlEntity(
                id = entity.crossControl?.id?: UUID.randomUUID(),
                type = entity.crossControl?.type,
                agentId = entity.crossControl?.agentId,
                vesselId = entity.crossControl?.vesselId,
                serviceId = entity.crossControl?.serviceId,
                endDateTimeUtc = entity.endDateTimeUtc,
                startDateTimeUtc = entity.startDateTimeUtc,
                origin = entity.crossControl?.origin,
                status = entity.crossControl?.status,
                conclusion = entity.crossControl?.conclusion,
            )
        }
    }
}
