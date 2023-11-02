package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.action
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
    fun execute(missionId: Int, actionStartDateTimeUtc: ZonedDateTime?): ActionStatusType {
        val actions = statusActionsRepository.findAllByMissionId(missionId=missionId).map { it.toActionStatusEntity() }

        if (actions.isNullOrEmpty()) {
            return ActionStatusType.UNKNOWN
        }

        val lastActionStatus = actions
            .filter { it.startDateTimeUtc <= actionStartDateTimeUtc }
            .maxByOrNull { it.startDateTimeUtc }
        val lastStartedActionStatus = actions
            .filter { it.isStart && it.startDateTimeUtc <= actionStartDateTimeUtc }
            .maxByOrNull { it.startDateTimeUtc }


        // case where there are 2 statuses at the same timestamp, one starting, one ending:
        if (lastActionStatus != null && lastStartedActionStatus != null && lastStartedActionStatus.startDateTimeUtc == lastActionStatus.startDateTimeUtc) {
            return lastStartedActionStatus.status
        }
        // return unknown if no action or if last action is a status of type finishing and no other starting action at that timestamp
        else if (lastActionStatus == null || (!lastActionStatus.isStart && lastStartedActionStatus?.startDateTimeUtc != lastActionStatus.startDateTimeUtc)) {
            return ActionStatusType.UNKNOWN
        }
        // return status of last status, it implies it is a starting status
        else {
            return lastActionStatus.status
        }
    }

}
