package fr.gouv.dgampa.rapportnav.domain.use_cases.inquiry

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.IInquiryRepository
import java.time.Instant
import java.time.ZoneOffset

@UseCase
class GetInquiriesByVesselId(
    private val inquiryRepo: IInquiryRepository
) {
    fun execute(vesselId: Int, startDateTimeUtc: Instant? = null): List<InquiryEntity> {
        return inquiryRepo.findByVesselId(
            vesselId = vesselId,
            startDateTimeUtc = startDateTimeUtc ?: defaultStartDateTimeUtc()
        ).map { InquiryEntity.fromInquiryModel(it) }
    }

    /**
     * Same default depth as the MonitorFish vessel sidebar: start of the year, 5 years ago.
     */
    fun defaultStartDateTimeUtc(now: Instant = Instant.now()): Instant {
        return now.atZone(ZoneOffset.UTC)
            .minusYears(DEFAULT_DEPTH_IN_YEARS)
            .withDayOfYear(1)
            .toLocalDate()
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
    }

    companion object {
        const val DEFAULT_DEPTH_IN_YEARS = 5L
    }
}
