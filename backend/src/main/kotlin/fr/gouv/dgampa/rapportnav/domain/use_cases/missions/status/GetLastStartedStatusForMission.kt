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
        val lastStartActionStatus = actions
            .filter { it.isStart && it.startDateTimeUtc <= actionStartDateTimeUtc }
            .maxByOrNull { it.startDateTimeUtc }

        if (lastStartActionStatus != null) {
            return lastStartActionStatus.status
        }
        else {
            val lastAction = actions.last()
            if (lastAction.startDateTimeUtc == actionStartDateTimeUtc) {
                return lastAction.status
            }
            return ActionStatusType.UNAVAILABLE
        }
    }

}
