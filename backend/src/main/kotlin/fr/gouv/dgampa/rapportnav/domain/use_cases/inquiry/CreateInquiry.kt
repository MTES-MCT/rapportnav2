package fr.gouv.dgampa.rapportnav.domain.use_cases.inquiry

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.IInquiryRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Inquiry

@UseCase
class CreateInquiry(
    private val inquiryRepo: IInquiryRepository
) {
    fun execute(inquiry: Inquiry, serviceId: Int?): InquiryEntity {
        // Cloisonnement: ownership is set from the authenticated user's service, never trusted from the body.
        val model = inquiry.toInquiryEntity().toInquiryModel().apply { this.serviceId = serviceId }
        val saved = inquiryRepo.save(model = model)
        return InquiryEntity.fromInquiryModel(model = saved)
    }
}

