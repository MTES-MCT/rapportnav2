package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.*
import java.time.Instant
import java.util.*

class Inquiry(
    var id: UUID? = null,
    val agentId: Int? = null,
    val serviceId: Int? = null,
    var endDateTimeUtc: Instant? = null,
    val startDateTimeUtc: Instant? = null,
    val type: String? = null,
    val origin: InquiryOriginType? = null,
    val status: InquiryStatusType? = null,
    val conclusion: InquiryConclusionType? = null,
    val missionId: Int? = null,
    val missionIdUUID: UUID? = null,
    var actions: List<MissionNavAction?> = listOf(),
    var isSignedByInspector: Boolean? = null,
    val vessel: Vessel?= null,
    var establishment: Establishment? = null
) {

    fun toInquiryEntity(): InquiryEntity {
        return InquiryEntity(
            id = id,
            type = type,
            status = status,
            origin = origin,
            agentId = agentId,
            serviceId = serviceId,
            conclusion = conclusion,
            endDateTimeUtc = endDateTimeUtc,
            startDateTimeUtc = startDateTimeUtc,
            missionId = missionId,
            missionIdUUID = missionIdUUID,
            isSignedByInspector = isSignedByInspector,
            actions = listOf(),
            vessel = vessel?.toVesselEntity(),
            establishment = establishment?.toEstablishmentEntity()

        )
    }

    companion object {
        fun fromInquiryEntity(entity: InquiryEntity?): Inquiry {
            return Inquiry(
                id = entity?.id,
                type = entity?.type,
                status = entity?.status,
                origin = entity?.origin,
                agentId = entity?.agentId,
                serviceId = entity?.serviceId,
                conclusion = entity?.conclusion,
                endDateTimeUtc = entity?.endDateTimeUtc,
                startDateTimeUtc = entity?.startDateTimeUtc,
                missionId = entity?.missionId,
                missionIdUUID = entity?.missionIdUUID,
                isSignedByInspector = entity?.isSignedByInspector,
                vessel = entity?.vessel?.let { Vessel.fromVesselEntity(it) },
                establishment = entity?.establishment?.let { Establishment.fromEstablishmentEntity(it) },
                actions = entity?.actions?.map { action -> MissionNavAction.fromMissionActionEntity(action) } ?: listOf()
            )
        }
    }
}
