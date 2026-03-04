package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.passenger

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionPassengerEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionPassengerRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionDates
import org.slf4j.LoggerFactory

@UseCase
class UpdateMissionPassenger(
    private val repo: IMissionPassengerRepository,
    private val getMissionDates: GetMissionDates
) {
    private val logger = LoggerFactory.getLogger(UpdateMissionPassenger::class.java)

    fun execute(
        passenger: MissionPassengerEntity
    ): MissionPassengerEntity {
        // Validate passenger dates are within mission dates
        val missionDates = getMissionDates.execute(passenger.missionId, passenger.missionIdUUID)
        if (missionDates != null && !passenger.isWithinMissionDates(missionDates.startDateTimeUtc, missionDates.endDateTimeUtc)) {
            throw BackendUsageException(code = BackendUsageErrorCode.DATES_OUTSIDE_MISSION_RANGE_EXCEPTION)
        }

        val saved = repo.save(passenger)
        return MissionPassengerEntity.fromMissionPassengerModel(saved)
    }
}
