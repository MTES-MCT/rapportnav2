package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.inquiry.GetInquiryById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetEnvMissionById
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
    private val getNavMissionById: GetNavMissionById,
    private val getEnvMissionById: GetEnvMissionById,
    private val getInquiryById: GetInquiryById
) {

    fun execute(missionId: Int?, ownerId: UUID?, inquiryId: UUID? = null): MissionDatesOutput? {

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
        // Try to get dates from Nav mission (local mission stored by UUID)
        if (ownerId != null) {
            val navMission = getNavMissionById.execute(ownerId)
            if (navMission != null) {
                return MissionDatesOutput(
                    startDateTimeUtc = navMission.startDateTimeUtc,
                    endDateTimeUtc = navMission.endDateTimeUtc
                )
            }
        }

        // Try to get dates from Env mission (external mission by Int ID)
        if (missionId != null) {
            val envMission = getEnvMissionById.execute(missionId)
            if (envMission != null) {
                return MissionDatesOutput(
                    startDateTimeUtc = envMission.startDateTimeUtc,
                    endDateTimeUtc = envMission.endDateTimeUtc
                )
            }
        }

        return null
    }
}
