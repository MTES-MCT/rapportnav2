package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionStatusRepository
import org.slf4j.LoggerFactory
import java.time.ZonedDateTime

@UseCase
class GetStatusForAction(
    private val statusActionsRepository: INavActionStatusRepository
) {
    private val logger = LoggerFactory.getLogger(GetStatusForAction::class.java)
    fun execute(missionId: Int, actionStartDateTimeUtc: ZonedDateTime? = null): ActionStatusType {
        val actions =
            statusActionsRepository.findAllByMissionId(missionId = missionId).map { it.toActionStatusEntity() }

        if (actions.isNullOrEmpty()) {
            return ActionStatusType.UNKNOWN
        }

        val lastActionStatus = actions
            .filter { it.startDateTimeUtc <= actionStartDateTimeUtc }
            .maxByOrNull { it.startDateTimeUtc }

        return lastActionStatus?.status ?: ActionStatusType.UNKNOWN
    }

}
