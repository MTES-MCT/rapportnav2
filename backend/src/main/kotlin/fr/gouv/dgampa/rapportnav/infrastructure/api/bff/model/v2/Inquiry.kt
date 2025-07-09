package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.*
import java.time.Instant
import java.util.*

class Inquiry(
    var id: UUID? = null,
    val agentId: Int? = null,
    val vesselId: Int? = null,
    val serviceId: Int? = null,
    var endDateTimeUtc: Instant? = null,
    val startDateTimeUtc: Instant? = null,
    val type: String? = null,
    val origin: InquiryOriginType? = null,
    val status: InquiryStatusType? = null,
    val conclusion: InquiryConclusionType? = null,
    val vesselName: String? = null,
    val vesselExternalReferenceNumber: String? = null,
    val missionId: Int? = null,
    val missionIdUUID: UUID? = null,
    var actions: List<MissionNavAction?> = listOf(),
) {

    fun toInquiryEntity(): InquiryEntity {
        return InquiryEntity(
            id = id,
            type = type,
            status = status,
            origin = origin,
            agentId = agentId,
            vesselId = vesselId,
            serviceId = serviceId,
            conclusion = conclusion,
            vesselName = vesselName,
            endDateTimeUtc = endDateTimeUtc,
            startDateTimeUtc = startDateTimeUtc,
            missionId = missionId,
            missionIdUUID = missionIdUUID,
            vesselExternalReferenceNumber = vesselExternalReferenceNumber,
            actions = listOf()
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
                vesselId = entity?.vesselId,
                serviceId = entity?.serviceId,
                conclusion = entity?.conclusion,
                vesselName = entity?.vesselName,
                endDateTimeUtc = entity?.endDateTimeUtc,
                startDateTimeUtc = entity?.startDateTimeUtc,
                missionId = entity?.missionId,
                missionIdUUID = entity?.missionIdUUID,
                vesselExternalReferenceNumber = entity?.vesselExternalReferenceNumber,
                actions = entity?.actions?.map { action -> MissionNavAction.fromMissionActionEntity(action) } ?: listOf()
            )
        }
    }
}
