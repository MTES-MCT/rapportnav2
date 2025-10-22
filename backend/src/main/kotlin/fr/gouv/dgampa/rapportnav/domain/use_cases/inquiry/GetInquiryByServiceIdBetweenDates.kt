package fr.gouv.dgampa.rapportnav.domain.use_cases.inquiry

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.IInquiryRepository
import java.time.Instant

@UseCase
class GetInquiryByServiceIdBetweenDates(
    private val inquiryRepo: IInquiryRepository
) {
    fun execute(serviceId: Int?, startDateTimeUtc: Instant, endDateTimeUtc: Instant): List<InquiryEntity> {
        if(serviceId == null) return emptyList()
        return inquiryRepo.findAll(startBeforeDateTime = startDateTimeUtc, endBeforeDateTime = endDateTimeUtc)
            .filterNotNull()
            .filter { it.serviceId == serviceId }
            .map { InquiryEntity.fromInquiryModel(it) }
    }
}
