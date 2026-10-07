package fr.gouv.dgampa.rapportnav.domain.use_cases.inquiry

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.IInquiryRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Inquiry
import java.util.*
import kotlin.jvm.optionals.getOrNull

@UseCase
class UpdateInquiry(
    private val inquiryRepo: IInquiryRepository
) {
    fun execute(id: UUID? = null, inquiry: Inquiry): InquiryEntity? {
        if (id == null || inquiry.id != id) return null
        val existing = inquiryRepo.findById(id = id).getOrNull() ?: return null
        // Cloisonnement: ownership is immutable through update — keep the existing row's service, ignore the body.
        val model = inquiry.toInquiryEntity().toInquiryModel().apply { this.serviceId = existing.serviceId }
        val saved = inquiryRepo.save(model = model)
        return InquiryEntity.fromInquiryModel(model = saved)
    }
}

