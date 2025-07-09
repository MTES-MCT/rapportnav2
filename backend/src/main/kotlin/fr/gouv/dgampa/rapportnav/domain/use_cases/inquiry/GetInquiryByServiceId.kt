package fr.gouv.dgampa.rapportnav.domain.use_cases.inquiry

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.IInquiryRepository

@UseCase
class GetInquiryByServiceId(
    private val inquiryRepo: IInquiryRepository
) {
    fun execute(serviceId: Int?): List<InquiryEntity> {
        if(serviceId == null) return emptyList()
        return inquiryRepo
            .findByServiceId(serviceId = serviceId)
            .map { InquiryEntity.fromInquiryModel(it) }
    }
}
