package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionDates
import fr.gouv.dgampa.rapportnav.domain.utils.MissionDateUtils
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavActionData
import java.time.Instant

@UseCase
class CreateNavAction(
    private val missionActionRepository: INavMissionActionRepository,
    private val getMissionDates: GetMissionDates
) {
    fun execute(input: MissionAction): MissionNavActionEntity {
        val action = MissionNavActionData.toMissionNavActionEntity(input)

        val missionDates = getMissionDates.execute(
            missionId = action.missionId,
            ownerId = action.ownerId
        ) ?: throw BackendInternalException(
            message = "Could not retrieve mission dates for missionId=${action.missionId}"
        )

        fun requireInRange(date: Instant) {
            if (!MissionDateUtils.isDateWithinMissionRange(date, missionDates.startDateTimeUtc, missionDates.endDateTimeUtc))
                throw BackendUsageException(BackendUsageErrorCode.DATES_OUTSIDE_MISSION_RANGE_EXCEPTION)
        }

        val startDate = action.startDateTimeUtc
            ?: throw BackendUsageException(BackendUsageErrorCode.DATES_OUTSIDE_MISSION_RANGE_EXCEPTION, "Date de début requise")

        requireInRange(startDate)
        action.endDateTimeUtc?.let { requireInRange(it) }

        return MissionNavActionEntity.fromMissionActionModel(missionActionRepository.save(action.toMissionActionModel()))
    }
}
