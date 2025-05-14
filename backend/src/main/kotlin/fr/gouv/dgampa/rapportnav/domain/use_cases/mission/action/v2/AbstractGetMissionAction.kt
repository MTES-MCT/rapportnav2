package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction2

abstract class AbstractGetMissionAction(
    private val getStatusForAction: GetStatusForAction2
) {
    fun getStatus(action: MissionActionEntity): ActionStatusType? {
        if (action.status != null) return action.status
        return getStatusForAction.execute(
            missionId = action.missionId,
            actionStartDateTimeUtc = action.startDateTimeUtc
        )
    }
}
