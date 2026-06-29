package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.inquiry.GetInquiryById
import java.time.Instant
import java.util.*

data class MissionDatesOutput(
    val startDateTimeUtc: Instant?,
    val endDateTimeUtc: Instant?
) {
    /**
     * Returns true if the mission is finished (end date is in the past).
     */
    fun isMissionFinished(): Boolean = endDateTimeUtc?.isBefore(Instant.now()) ?: false
}

@UseCase
class GetMissionDates(
    private val getNavMissionById2: GetNavMissionById2,
    private val getInquiryById: GetInquiryById
) {

    fun execute(missionId: UUID?, ownerId: UUID? = null, inquiryId: UUID? = null): MissionDatesOutput? {

        // Try to get from Inquiry table if inquiryId is provided
        if (inquiryId != null) {
            val inquiry: InquiryEntity? = inquiryId.let { getInquiryById.execute(id = it) }
            if (inquiry != null) {
                return MissionDatesOutput(
                    startDateTimeUtc = inquiry.startDateTimeUtc,
                    endDateTimeUtc = inquiry.endDateTimeUtc
                )
            }
        }

        // Try to get dates from local mission table (by UUID)
        if (missionId != null) {
            val navMission = getNavMissionById2.execute(missionId)
            if (navMission != null) {
                return MissionDatesOutput(
                    startDateTimeUtc = navMission.startDateTimeUtc,
                    endDateTimeUtc = navMission.endDateTimeUtc
                )
            }
        }

        // Try to get dates from Nav mission by ownerId
        if (ownerId != null) {
            val navMission = getNavMissionById2.execute(ownerId)
            if (navMission != null) {
                return MissionDatesOutput(
                    startDateTimeUtc = navMission.startDateTimeUtc,
                    endDateTimeUtc = navMission.endDateTimeUtc
                )
            }
        }

        return null
    }
}
