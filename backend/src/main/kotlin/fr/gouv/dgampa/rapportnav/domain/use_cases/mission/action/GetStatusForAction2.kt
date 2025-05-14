package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBMissionActionRepository
import org.slf4j.LoggerFactory
import java.time.Instant

@UseCase
class GetStatusForAction2(
    private val missionActionsRepository: IDBMissionActionRepository,

) {
    private val logger = LoggerFactory.getLogger(GetStatusForAction2::class.java)

    fun execute(missionId: Int, actionStartDateTimeUtc: Instant? = null): ActionStatusType {
        val actions =
            missionActionsRepository.findAllByMissionId(missionId = missionId)
                .map {
                MissionNavActionEntity.fromMissionActionModel(it)
            }

        if (actions.isEmpty()) {
            return ActionStatusType.UNKNOWN
        }

        val lastActionStatus = actions
            .filter {
                it.actionType == ActionType.STATUS &&
                it.startDateTimeUtc != null &&
                it.startDateTimeUtc!! <= actionStartDateTimeUtc
            }
            .maxByOrNull { it.startDateTimeUtc!! }


        return lastActionStatus?.status ?: ActionStatusType.UNKNOWN
    }

}
