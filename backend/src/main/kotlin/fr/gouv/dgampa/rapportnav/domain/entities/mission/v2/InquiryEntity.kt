package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.InquiryModel
import java.time.Instant
import java.util.*

class InquiryEntity(
    val id: UUID? = null,
    val missionId: Int? = null,
    val missionIdUUID: UUID? = null,
    val type: String? = null,
    val agentId: Int? = null,
    val vesselId: Int? = null,
    val serviceId: Int? = null,
    var endDateTimeUtc: Instant? = null,
    val startDateTimeUtc: Instant? = null,
    val origin: InquiryOriginType? = null,
    val status: InquiryStatusType? = null,
    val conclusion: InquiryConclusionType? = null,
    var vesselName: String? = null,
    var vesselExternalReferenceNumber: String? = null,
    var actions: List<MissionActionEntity>? = null,
    var isSignedByInspector: Boolean? = null,
) {

    companion object {
        fun fromInquiryModel(model: InquiryModel): InquiryEntity {
            return InquiryEntity(
                id = model.id,
                type = model.type,
                missionId = model.missionId,
                missionIdUUID = model.missionIdUUID,
                vesselId = model.vesselId,
                serviceId = model.serviceId,
                agentId = model.agentId?.toInt(),
                endDateTimeUtc = model.endDateTimeUtc,
                startDateTimeUtc = model.startDateTimeUtc,
                origin = model.origin?.let { InquiryOriginType.valueOf(it) },
                status = model.status?.let { InquiryStatusType.valueOf(it) },
                conclusion = model.conclusion?.let { InquiryConclusionType.valueOf(it) },
                isSignedByInspector = model.isSignedByInspector
            )
        }
    }

    fun toInquiryModel(): InquiryModel {
        return InquiryModel(
            id = id ?: UUID.randomUUID(),
            type = type,
            agentId = agentId?.toString(),
            serviceId = serviceId,
            vesselId = vesselId,
            status = status?.toString(),
            origin = origin?.toString(),
            endDateTimeUtc = endDateTimeUtc,
            startDateTimeUtc = startDateTimeUtc!!,
            conclusion = conclusion?.toString(),
            missionId = missionId,
            missionIdUUID = missionIdUUID,
            isSignedByInspector = isSignedByInspector
        )
    }

    fun withVessel(vessel: VesselEntity?): InquiryEntity {
        this.vesselName = vessel?.vesselName
        this.vesselExternalReferenceNumber = vessel?.externalReferenceNumber
        return this
    }

    fun withActions(actions: List<MissionActionEntity>?): InquiryEntity {
        this.actions = actions
        return this
    }

}
