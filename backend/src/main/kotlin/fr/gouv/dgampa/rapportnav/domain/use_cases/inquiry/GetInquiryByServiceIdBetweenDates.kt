package fr.gouv.dgampa.rapportnav.domain.use_cases.inquiry

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.IInquiryRepository
import java.time.Instant
import java.time.ZoneOffset

@UseCase
class GetInquiryByServiceIdBetweenDates(
    private val inquiryRepo: IInquiryRepository
) {
    fun execute(serviceId: Int?, startDateTimeUtc: Instant, endDateTimeUtc: Instant? = null): List<InquiryEntity> {
        if(serviceId == null) return emptyList()
        return inquiryRepo.findAll(
            startBeforeDateTime = startDateTimeUtc, endBeforeDateTime = endDateTimeUtc ?: Instant.now()
                .atZone(ZoneOffset.UTC)
                .plusMonths(1)
                .withDayOfMonth(1)
                .toInstant()
        )
            .filterNotNull()
            .filter { it.serviceId == serviceId }
            .map { InquiryEntity.fromInquiryModel(it) }
    }
}
