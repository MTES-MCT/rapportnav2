package fr.gouv.dgampa.rapportnav.domain.use_cases.inquiry

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.IInquiryRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Inquiry

@UseCase
class CreateInquiry(
    private val inquiryRepo: IInquiryRepository
) {
    fun execute(inquiry: Inquiry): InquiryEntity {
        val entity = inquiry.toInquiryEntity()
        val model = inquiryRepo.save(model = entity.toInquiryModel())
        return InquiryEntity.fromInquiryModel(model = model)
    }
}

