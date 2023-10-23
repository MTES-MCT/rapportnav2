package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.status
import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionStatusRepository
import org.slf4j.LoggerFactory
import java.time.ZonedDateTime

@UseCase
class GetLastStartedStatusForMission(
    private val statusActionsRepository: INavActionStatusRepository
) {
    private val logger = LoggerFactory.getLogger(GetLastStartedStatusForMission::class.java)
    fun execute(missionId: Int, actionStartDateTimeUtc: ZonedDateTime?): ActionStatusType {
        val actions = statusActionsRepository.findAllByMissionId(missionId=missionId)
        val lastActionStatus = actions
            .filter { it.startDateTimeUtc <= actionStartDateTimeUtc }
            .maxByOrNull { it.startDateTimeUtc }

        // no status to return if no action or if last action is a status of type finishing
        if (lastActionStatus == null || !lastActionStatus.isStart) {
            return ActionStatusType.UNKNOWN
        }
        // status to return if last action is of type starting
        else {
//            val lastActionAmongAll = actions.last()
            // special case
//            if (lastActionAmongAll.startDateTimeUtc == actionStartDateTimeUtc) {
//                return lastActionAmongAll.status
//            }
//            else {
//                return lastActionStatus.status
//            }
            return lastActionStatus.status
        }
    }

}
