package fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.inquiry.v1.adapters.output

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryEntity

object InquiryOutputMapper {

    fun toOutput(entity: InquiryEntity, serviceNamesById: Map<Int, String> = emptyMap()): InquiryOutput {
        return InquiryOutput(
            id = requireNotNull(entity.id) { "InquiryEntity.id must be set to build an InquiryOutput" },
            vesselId = entity.vessel?.vesselId,
            type = entity.type,
            origin = entity.origin,
            status = entity.status,
            conclusion = entity.conclusion,
            startDateTimeUtc = entity.startDateTimeUtc,
            endDateTimeUtc = entity.endDateTimeUtc,
            service = entity.serviceId?.let { InquiryServiceOutput(id = it, name = serviceNamesById[it]) }
        )
    }
}
