package fr.gouv.dgampa.rapportnav.domain.use_cases.inquiry

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.IInquiryRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ITargetRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetNavActionListByOwnerId
import java.time.Instant
import java.time.ZoneOffset
import java.util.*

@UseCase
class GetInquiriesByVesselId(
    private val inquiryRepo: IInquiryRepository,
    private val getNavActionListByOwnerId: GetNavActionListByOwnerId,
    private val targetRepo: ITargetRepository
) {
    fun execute(vesselId: Int, startDateTimeUtc: Instant? = null): List<InquiryEntity> {
        return inquiryRepo.findByVesselId(
            vesselId = vesselId,
            startDateTimeUtc = startDateTimeUtc ?: defaultStartDateTimeUtc()
        ).map { InquiryEntity.fromInquiryModel(it).withActions(findWorkSessions(it.id)) }
    }

    private fun findWorkSessions(inquiryId: UUID): List<MissionNavActionEntity> {
        return getNavActionListByOwnerId.execute(ownerId = inquiryId).onEach { it.targets = findTargets(it) }
    }

    private fun findTargets(action: MissionNavActionEntity): List<TargetEntity> {
        return targetRepo.findByActionId(action.getActionId()).map { TargetEntity.fromTargetModel(it) }
    }

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
