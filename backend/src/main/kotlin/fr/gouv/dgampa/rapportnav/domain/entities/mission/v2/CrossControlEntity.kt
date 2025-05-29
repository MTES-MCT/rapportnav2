package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.CrossControlModel
import java.time.Instant
import java.util.*

class CrossControlEntity(
    val id: UUID? = null,
    val type: String? = null,
    val agentId: Int? = null,
    val vesselId: Int? = null,
    val serviceId: Int? = null,
    var sumNbrOfHours: Int? = null,
    var endDateTimeUtc: Instant? = null,
    val startDateTimeUtc: Instant? = null,
    val origin: CrossControlOriginType? = null,
    val status: CrossControlStatusType? = null,
    val conclusion: CrossControlConclusionType? = null,
    var vesselName: String? = null,
    var vesselExternalReferenceNumber: String? = null,
) {

    companion object {
        fun fromCrossControlModel(model: CrossControlModel): CrossControlEntity {
            return CrossControlEntity(
                id = model.id,
                type = model.type,
                vesselId = model.vesselId,
                serviceId = model.serviceId,
                agentId = model.agentId?.toInt(),
                endDateTimeUtc = model.endDateTimeUtc,
                startDateTimeUtc = model.startDateTimeUtc,
                origin = model.origin?.let { CrossControlOriginType.valueOf(it) },
                status = model.status?.let { CrossControlStatusType.valueOf(it) },
                conclusion = model.conclusion?.let { CrossControlConclusionType.valueOf(it) },
            )
        }
    }

    fun toCrossControlModel(): CrossControlModel {
        return CrossControlModel(
            id = id ?: UUID.randomUUID(),
            type = type,
            agentId = agentId?.toString(),
            serviceId = serviceId,
            vesselId = vesselId,
            status = status?.toString(),
            origin = origin?.toString(),
            endDateTimeUtc = endDateTimeUtc,
            startDateTimeUtc = startDateTimeUtc!!,
            conclusion = conclusion?.toString()
        )
    }

    fun withVessel(vessel: VesselEntity?) {
        this.vesselName = vessel?.vesselName
        this.vesselExternalReferenceNumber = vessel?.externalReferenceNumber
    }

    fun toModelSetData(entity: CrossControlEntity): CrossControlModel {
        return CrossControlModel(
            id = id!!,
            type = entity.type,
            serviceId = entity.serviceId,
            vesselId = entity.vesselId,
            status = status?.toString(),
            endDateTimeUtc = endDateTimeUtc,
            origin = entity.origin?.toString(),
            conclusion = conclusion?.toString(),
            agentId = entity.agentId?.toString(),
            startDateTimeUtc = entity.startDateTimeUtc!!
        )
    }

    fun toModelSetConclusion(entity: CrossControlEntity): CrossControlModel {
        return CrossControlModel(
            id = id!!,
            type = type,
            agentId = agentId?.toString(),
            serviceId = serviceId,
            vesselId = vesselId,
            origin = origin?.toString(),
            status = entity.status?.toString(),
            startDateTimeUtc = startDateTimeUtc!!,
            endDateTimeUtc = if(entity.status == CrossControlStatusType.CLOSED) entity.endDateTimeUtc else endDateTimeUtc,
            conclusion = if(entity.status == CrossControlStatusType.CLOSED) entity.conclusion?.toString() else conclusion?.toString()
        )
    }
}
